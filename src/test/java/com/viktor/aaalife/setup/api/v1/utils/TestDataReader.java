package com.viktor.aaalife.setup.api.v1.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public final class TestDataReader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private TestDataReader() {
    }

    public static <T> T readJson(String resourcePath, TypeReference<T> typeReference) {
        try (InputStream inputStream = TestDataReader.class
                .getClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (inputStream == null) {
                throw new IllegalArgumentException("Test data file not found: " + resourcePath);
            }

            return OBJECT_MAPPER.readValue(inputStream, typeReference);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read test data from: " + resourcePath, e);
        }
    }
}