package com.viktor.aaalife.setup.api.v1.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public final class SchemaValidator {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private SchemaValidator() {
    }

    public static void assertMatchesSchema(Response response, String schemaPath) {
        response.then().assertThat()
                .body(matchesJsonSchemaInClasspath(schemaPath));
    }

    public static void assertJsonMatchesSchema(String jsonPayload, String schemaPath) {
        assertThat(jsonPayload, matchesJsonSchemaInClasspath(schemaPath));
    }

    public static void assertObjectMatchesSchema(Object payload, String schemaPath) {
        try {
            assertJsonMatchesSchema(OBJECT_MAPPER.writeValueAsString(payload), schemaPath);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unable to serialize payload before schema validation", e);
        }
    }
}
