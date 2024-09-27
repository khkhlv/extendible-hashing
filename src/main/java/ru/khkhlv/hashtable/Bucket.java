package ru.khkhlv.hashtable;

import one.nio.serial.CalcSizeStream;
import ru.khkhlv.data.Data;
import ru.khkhlv.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bucket {
    private int localDepth;
    private final int maxBucketSize = Integer.parseInt(Utils.getAppProperties().getProperty("BUCKET_SIZE"));
    private final List<Data> bucketData = new ArrayList<>();
    private final int maxLocalDepth = Integer.parseInt(Utils.getAppProperties().getProperty("BUCKET_MAX_DEPTH"));;
    private int occupiedBytes;

    public Bucket(int localDepth) {
        this.localDepth = localDepth;
    }

    public int getLocalDepth() {
        return localDepth;
    }

    public void setLocalDepth(int localDepth) {
        this.localDepth = localDepth;
    }

    public List<Data> getBucketData() {
        return bucketData;
    }

    public int search(Data data) {
        if (getBucketData().contains(data)) {
            System.out.println("Key exists in a bucket");
            return 1;
        } else {
            System.out.println("This key does not exists");
            return 0;
        }
    }

    public int insert(Data data) {
        if (this.localDepth < maxLocalDepth) {
            bucketData.add(data);
            return bucketData.size() - 1;
        } else {
            return -1;
        }
    }

    public void clear() {
        this.occupiedBytes = 0;
        this.bucketData.clear();
    }

    public boolean isBucketHaveFreeSpace(Data data) {
        int dataBytes = getDataSize(data);
        return occupiedBytes + dataBytes < maxBucketSize;
    }

    private int getDataSize(Data data) {
        CalcSizeStream css = new CalcSizeStream();
        try {
            css.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return css.count();
    }
}
