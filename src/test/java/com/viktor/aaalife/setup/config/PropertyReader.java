package com.viktor.aaalife.setup.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertyReader {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = PropertyReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {

            if (inputStream == null) {
                throw new IllegalStateException("Config file not found: " + CONFIG_FILE);
            }

            properties.load(inputStream);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load config file: " + CONFIG_FILE, e);
        }
    }

    private PropertyReader() {
    }

    public static String get(String key) {
        String systemPropertyValue = System.getProperty(key);

        if (systemPropertyValue != null && !systemPropertyValue.isBlank()) {
            return systemPropertyValue.trim();
        }

        String environmentVariableKey = key.toUpperCase().replace(".", "_");
        String environmentVariableValue = System.getenv(environmentVariableKey);

        if (environmentVariableValue != null && !environmentVariableValue.isBlank()) {
            return environmentVariableValue.trim();
        }

        String propertyValue = properties.getProperty(key);

        if (propertyValue == null || propertyValue.isBlank()) {
            throw new IllegalStateException("Missing required config property: " + key);
        }

        return propertyValue.trim();
    }

    public static String getOrDefault(String key, String defaultValue) {
        try {
            return get(key);
        } catch (IllegalStateException e) {
            return defaultValue;
        }
    }
}