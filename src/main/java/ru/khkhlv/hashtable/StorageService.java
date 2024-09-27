package ru.khkhlv.hashtable;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

import one.nio.serial.DeserializeStream;
import one.nio.serial.PersistStream;
import one.nio.serial.SerializeStream;
import ru.khkhlv.data.Data;
import ru.khkhlv.utils.Utils;
import org.apache.commons.io.*;

public class StorageService {
    // Используем one-nio для сериализации и десериализации

    public void writeToFile(Directory data, String fileName) {
        int bufferSize = Integer.parseInt(Utils.getAppProperties().getProperty("BUCKET_SIZE"));
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileName), bufferSize);
             PersistStream out = new PersistStream(bufferSize)) {
            out.writeObject(data);
            outputStream.write(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object readFromFile(String fileName) {
        try {
            byte[] buf = FileUtils.readFileToByteArray(new File(fileName));
            DeserializeStream in = new DeserializeStream(buf);
            try {
                return in.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
