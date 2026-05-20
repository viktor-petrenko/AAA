package com.viktor.aaalife.setup.api.v1.base;

import com.viktor.aaalife.setup.api.v1.clients.RestfulBookerClient;
import com.viktor.aaalife.setup.config.PropertyReader;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public abstract class ApiBaseTest {
    private RestfulBookerClient client;

    @BeforeClass
    public void apiBaseSetUp() {
        RestAssured.baseURI = PropertyReader.get("api.base.url");
        client = new RestfulBookerClient();
    }

    protected RestfulBookerClient client() {
        return client;
    }
}
