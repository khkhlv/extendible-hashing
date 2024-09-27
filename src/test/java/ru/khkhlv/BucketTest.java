package ru.khkhlv;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.khkhlv.data.Data;
import ru.khkhlv.hashtable.Bucket;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class BucketTest {
    private Bucket bucket;


    private HashMap<Integer, Object> hashMap = new HashMap<>();

    @BeforeEach
    public void setUp() {
        bucket = new Bucket(1);
        hashMap.put(1, "abc");
        hashMap.put(2, "cdf");
        hashMap.put(5, "klm");
    }

    @Test
    public void testInsert() {
        Data rec_1 = new Data(1, hashMap);
        Data rec_2 = new Data(2, hashMap);
        Data rec_3 = new Data(3, hashMap);
        bucket.insert(rec_1);
        bucket.insert(rec_2);
        bucket.insert(rec_3);
        //assertEquals(3, bucket.getKeys().size());
        assertEquals(1, bucket.getBucketData().get(0).id());
    }

    @Test
    public void testClear() {
        Data rec_1 = new Data(1, hashMap);
        Data rec_2 = new Data(2, hashMap);
        Data rec_3 = new Data(3, hashMap);
        bucket.insert(rec_1);
        bucket.insert(rec_2);
        bucket.insert(rec_3);
        bucket.clear();
        assertEquals(0, bucket.getBucketData().size());
    }
}
