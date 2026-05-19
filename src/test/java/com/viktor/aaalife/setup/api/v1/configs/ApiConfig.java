package com.viktor.aaalife.setup.api.v1.configs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class ApiConfig {

    private ApiConfig() {
        // Utility class. Prevents creating object from this class.
    }

    private static final String DEFAULT_BASE_URI = "https://restful-booker.herokuapp.com";

    public static RequestSpecification defaultRequestSpec() {
        String baseUri = System.getProperty("api.baseUri", DEFAULT_BASE_URI);

        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setConfig(io.restassured.config.RestAssuredConfig.config()
                        .logConfig(LogConfig.logConfig()
                                .enableLoggingOfRequestAndResponseIfValidationFails()))
                .build();
    }
}