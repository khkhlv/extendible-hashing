package ru.khkhlv.benchmark;

import org.apache.log4j.BasicConfigurator;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.khkhlv.data.Data;
import ru.khkhlv.hashtable.StorageService;
import ru.khkhlv.hashtable.ExtendableHashTable;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


@BenchmarkMode({Mode.AverageTime, Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3, time = 100, timeUnit = TimeUnit.MILLISECONDS)
public class BenchmarkHashing {
    @Param({"10", "100", "1000"})
    private int N;

    int bucketBSize = 2048;
    private ExtendableHashTable<Integer, String> extendableHashTable;

    private String fileName = "serializedDir.ser";

    private static final Logger log = LoggerFactory.getLogger(BenchmarkHashing.class);

    private String tempStr = "tempValue";
    private final HashMap<String, Object> hashMap = new HashMap<>();

    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(BenchmarkHashing.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Setup
    public void setUp() {
        org.apache.log4j.BasicConfigurator.configure();
        org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);
        extendableHashTable = new ExtendableHashTable<>(bucketBSize);
        hashMap.put("key1", "value1");
        hashMap.put("key2", "value2");
        for (int i = 0; i < N; i++) {
            String endStr = "_" + (i + 1);
            extendableHashTable.put(new Data(i + 1, tempStr + endStr));
        }
    }

    @TearDown(Level.Iteration)
    public void teardown() {
        System.gc();
    }

    @Benchmark
    public void insertElement(Blackhole bh) {
        for (int i = 0; i < N; i++) {
            String endStr = "_" + (i + 1);
            extendableHashTable.put(new Data(i + 1, tempStr + endStr));
        }

        for (int i = 0; i < N; i++) {
            String endStr = "_" + (i + 1);
            extendableHashTable.remove(new Data(i + 1, tempStr + endStr));
        }
        bh.consume(extendableHashTable);
    }

    @Benchmark
    public void searchElement(Blackhole bh) {
        for (int i = 0; i < N; i++) {
            String endStr = "_" + (i + 1);
            extendableHashTable.search(new Data(i + 1, tempStr + endStr));
        }
        bh.consume(extendableHashTable);
    }

    @Benchmark
    public void serialize(Blackhole bh) throws IOException {

        StorageService storageService = new StorageService();
        for (int i = 0; i < N; i++) {

            // гасим warn log4j
            BasicConfigurator.configure();
            org.apache.log4j.Logger.getRootLogger().setLevel(org.apache.log4j.Level.INFO);


            // Сохраняем текущее состояние в файл
            log.info("Saving object ExtendibleHashing to file - " + fileName);
            storageService.writeToFile(extendableHashTable.getDirectory(), fileName);

            // Загружаем состояние из файла
            log.info("Loading object ExtendibleHashing from file - " + fileName);
            storageService.readFromFile(fileName);
            log.info("Load complete!");
        }
    }
}