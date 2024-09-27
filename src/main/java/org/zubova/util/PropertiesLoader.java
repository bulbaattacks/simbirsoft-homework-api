package org.zubova.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    public static Properties load() {
        var appProperties = new Properties();
        try (InputStream inputStream = PropertiesLoader.class
                .getClassLoader()
                .getResourceAsStream("application.properties")
        ) {
            appProperties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("File application.properties is not found");
        }
        var appEnvironment = System.getenv();
        appProperties.entrySet().forEach(entry -> {
            var appKey = entry.getKey();
            if (appEnvironment.containsKey(appKey)) {
                entry.setValue(appEnvironment.get(appKey));
            }
        });
        return appProperties;
    }

    private PropertiesLoader() {throw new IllegalStateException("Utility class");}
}
