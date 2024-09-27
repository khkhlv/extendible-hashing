package ru.khkhlv;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.khkhlv.data.Data;
import ru.khkhlv.hashtable.ExtendableHashTable;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class HashingTest {
    private ExtendableHashTable<Integer, Object> extendibleHashing;


    private final HashMap<String, String> hashMap = new HashMap<>();

    @BeforeEach
    public void setUp() {
        int bucketBSize = 1024;
        extendibleHashing = new ExtendableHashTable<>(bucketBSize);
        hashMap.put("key1", "value1");
        hashMap.put("key2", "value2");
    }

    @Test
    public void testGet() {
        extendibleHashing.put(1, hashMap);
        extendibleHashing.put(2, hashMap);
        assertEquals(extendibleHashing.get(1), hashMap);
    }

    @Test
    public void testPut() {
        extendibleHashing.put(1, hashMap);
        extendibleHashing.put(2, hashMap);

        extendibleHashing.put(new Data(3, hashMap));
        extendibleHashing.put(new Data(4, hashMap));

        assertTrue(extendibleHashing.getDirectory().buckets.get(1).getBucketData().contains(new Data(1, hashMap)));
        assertTrue(extendibleHashing.getDirectory().buckets.get(0).getBucketData().contains(new Data(4, hashMap)));
    }

    @Test
    public void testRemove() {
        extendibleHashing.put(new Data(5, hashMap));
        assertTrue(extendibleHashing.getDirectory().buckets.get(1).getBucketData().contains(new Data(5, hashMap)));
        extendibleHashing.remove(new Data(5, hashMap));
        assertFalse(extendibleHashing.getDirectory().buckets.get(1).getBucketData().contains(new Data(5, hashMap)));
    }
}