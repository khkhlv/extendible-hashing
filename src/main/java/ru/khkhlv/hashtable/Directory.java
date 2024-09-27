package ru.khkhlv.hashtable;


import ru.khkhlv.data.Data;
import ru.khkhlv.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Directory implements Serializable{

    private static final Logger log = LoggerFactory.getLogger(Directory.class);
    public List<Integer> listDirectoriesNumber;
    public List<Bucket> buckets;
    public int globalDepth = 4;
    private static int maxBytesBucketSize;

    public Directory(int capacity) {
        maxBytesBucketSize = capacity;
        listDirectoriesNumber = new ArrayList<>((int) Math.pow(2, globalDepth));
        buckets = new ArrayList<>();
        buckets.add(new Bucket(globalDepth));
        buckets.add(new Bucket(globalDepth));
        listDirectoriesNumber.add(0, 0);
        listDirectoriesNumber.add(1, 1);
    }

    public Data getData(int key) {
        int directoryId = Integer.parseInt(Utils.getBinaryStr(key, globalDepth), 2);
        int bucketIndex = listDirectoriesNumber.get(directoryId);
        Bucket bucket = buckets.get(bucketIndex);
        return bucket.getBucketData().get(key);
    }

    public void insert(Data data) {
        if (keyExists(data)) {
            // не разрешаем вставлять запись с ключом, который уже есть
            System.out.println("Key [" + data.id() + "] is already present and cannot be added.");
            return;
        }
        String directoryIdBinary = Utils.getBinaryStr(data.id(), globalDepth);
        int directoryKey = Integer.parseInt(directoryIdBinary, 2);
        //get bucket from directory
        int bucketIndex = listDirectoriesNumber.get(directoryKey);
        Bucket bucket = buckets.get(bucketIndex);
        if (bucket.insert(data) == -1) {
            splitBucket(directoryKey, data);
        }
    }


    private void splitBucket(int directoryKey, Data lastData) {
        Integer bucketIndex = listDirectoriesNumber.get(directoryKey);
        Bucket bucket = buckets.get(bucketIndex);
        if (bucket.getLocalDepth() == globalDepth) {
            expandDirectory();
        }
        if (bucket.getLocalDepth() < globalDepth) {
            // увеличиваем глубину существующего бакета на 1
            int newLocalDepth = bucket.getLocalDepth() + 1;
            bucket.setLocalDepth(newLocalDepth);
            // получаем все значения из бакета в новый List
            List<Data> bucketKeys = new ArrayList<>(bucket.getBucketData());
            // добавляем к значениям новое
            bucketKeys.add(lastData);
            // очищаем значения в бакете
            bucket.clear();
            // создаем новый бакет
            Bucket newBucket = new Bucket(newLocalDepth);
            buckets.add(newBucket);
            // запоминаем индекс нового бакета
            int newBucketIndex = buckets.size() - 1;
            //получаем все директории, которые ссылаются на этот бакет (хранятся индексы директорий)
            List <Integer> dirBuckets = new ArrayList<>();
            for (int i = 0; i< listDirectoriesNumber.size(); i++) {
                // если директория ссылается на нужный бакет, то сохраняем ее в список
                if (listDirectoriesNumber.get(i) == bucketIndex) {
                    dirBuckets.add(i);
                }
            }
            // бинарная строка для которой мы не меняем бакет
            String oldBinaryStr = Utils.getBinaryStr(directoryKey, newLocalDepth);
            // меняем бакеты для директорий, которые указывают на бакет для сплита
            for (int i = 0; i < dirBuckets.size(); i++) {
                String binStr = Utils.getBinaryStr(dirBuckets.get(i), newLocalDepth);
                if ( !binStr.equals(oldBinaryStr)) {
                    // Меняем бакет для директории на новый
                    listDirectoriesNumber.set(dirBuckets.get(i), newBucketIndex);

                }
            }
            // раскидываем значения по бакетам
            for (Data key : bucketKeys) {
                if (Utils.getBinaryStr(key.id(), newLocalDepth).equals(oldBinaryStr)) {
                    if (bucket.insert(key) == -1) {
                        log.error("Bucket is full");
                    }
                }
                else {
                    if (newBucket.insert(key) == -1) {
                        log.error("Bucket is full");
                    }
                }
            }
        }
    }

    public void remove(int key) {
        var data = getData(key);
        String directoryIdBinary = Utils.getBinaryStr(data.id(), globalDepth);
        int directoryKey = Integer.parseInt(directoryIdBinary, 2);
        //get bucket from directory
        int bucketIndex = listDirectoriesNumber.get(directoryKey);
        Bucket bucket = buckets.get(bucketIndex);

        if (bucket.getBucketData().contains(data)) {
            bucket.getBucketData().remove(data);
            System.out.println("Element " + data + " deleted successfully.");
        } else {
            System.out.println("Element " + data + " not found.");
        }
    }

    private void expandDirectory() {
        int currSize = listDirectoriesNumber.size();
        for (int i = currSize; i < currSize * 2; i++){
            //новые директории указывают на те же бакеты, что указывали старые
            listDirectoriesNumber.add(listDirectoriesNumber.get(i - currSize));
        }
        globalDepth++;
    }

    public Boolean keyExists(Data key) {
        int directoryId = Integer.parseInt(Utils.getBinaryStr(key.id(), globalDepth), 2);
        int bucketIndex = listDirectoriesNumber.get(directoryId);
        Bucket bucket = buckets.get(bucketIndex);
        for (Data item : bucket.getBucketData()) {
            if (item.id() == key.id()) {
                return  true;
            }
        }
        return  false;
    }

    public Boolean search(Data key) {
        int directoryId = Integer.parseInt(Utils.getBinaryStr(key.id(), globalDepth), 2);
        int bucketIndex = listDirectoriesNumber.get(directoryId);
        Bucket bucket = buckets.get(bucketIndex);
        return bucket.getBucketData().contains(key);
    }

    public  List<Integer> getlistDirectories() {
        return this.listDirectoriesNumber;
    }
}