package com.viktor.aaalife.setup.api.v1.configs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class ApiConfig {

    private ApiConfig() {
        // Utility class. Prevents creating object from this class.
    }

    public static RequestSpecification defaultRequestSpec() {

        return new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setConfig(io.restassured.config.RestAssuredConfig.config()
                        .logConfig(LogConfig.logConfig()
                                .enableLoggingOfRequestAndResponseIfValidationFails()))
                .build();
    }
}