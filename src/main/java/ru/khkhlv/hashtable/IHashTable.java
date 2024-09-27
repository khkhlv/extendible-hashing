package ru.khkhlv.hashtable;

import ru.khkhlv.data.Data;

public interface IHashTable<K, V> {
    void put(K key, V value);

    void put(Data data);

    void remove(Data key);

    Object get(K key);

    Object get(Data data);

    Boolean search(K key);

    Boolean search(Data data);
}
