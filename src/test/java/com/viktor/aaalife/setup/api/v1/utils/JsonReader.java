package com.viktor.aaalife.setup.api.v1.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonReader {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static <T> T readJson(String path, TypeReference<T> typeReference) {
        try (InputStream inputStream = BookingDataProvider.class
                .getClassLoader()
                .getResourceAsStream(path)) {

            if (inputStream == null) {
                throw new IllegalArgumentException("Test data file not found: " + path);
            }

            return OBJECT_MAPPER.readValue(inputStream, typeReference);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read booking test data from: " + path, e);
        }
    }
}
