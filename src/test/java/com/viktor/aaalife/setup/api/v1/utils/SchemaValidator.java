package com.viktor.aaalife.setup.api.v1.utils;

import io.restassured.response.Response;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public final class SchemaValidator {

    private SchemaValidator() {
    }

    public static void assertMatchesSchema(Response response, String schemaPath) {
        response.then().assertThat()
                .body(matchesJsonSchemaInClasspath(schemaPath));
    }
}