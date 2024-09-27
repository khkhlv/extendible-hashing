package ru.khkhlv.hashtable;

import ru.khkhlv.data.Data;

import java.io.Serializable;

public class ExtendableHashTable<K, V> implements IHashTable<K, V>, Serializable {

    private final Directory directory;

    public ExtendableHashTable(int capacity) {
        this.directory = new Directory(capacity);
    }

    public ExtendableHashTable(){
        this(8);
    }

    @Override
    public void put(K key, V value) {
        Data data = new Data(key.hashCode(), value);
        directory.insert(data);
    }

    @Override
    public Object get(K key) {
        Data data = directory.getData(key.hashCode());
        return data.data();
    }

    @Override
    public void remove(K key) {
        directory.remove(key.hashCode());
    }
}
