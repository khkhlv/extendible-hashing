package ru.khkhlv.hashtable;

public interface IHashTable<K, V> {
    void put(K key, V value);

    void remove(K key);

    Object get(K key);
}
