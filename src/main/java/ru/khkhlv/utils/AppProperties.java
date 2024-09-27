package ru.khkhlv.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AppProperties {

    private static AppProperties appProperties;
    private Properties properties;

    private AppProperties(){
    }

    public static AppProperties getInstance() { // #3
        if (appProperties == null) {        //если объект еще не создан
            appProperties = new AppProperties();    //создать новый объект
            appProperties.properties = getAppProperties();
        }
        return appProperties;
    }

    public Properties getProperties() {
        return properties;
    }

    private static Properties getAppProperties() {
        FileInputStream fis;
        Properties properties = new Properties();
        try {
            fis = new FileInputStream("src/main/resources/config.properties");
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        }
        return properties;
    }
}
