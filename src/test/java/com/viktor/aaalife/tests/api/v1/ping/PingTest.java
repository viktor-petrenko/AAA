package com.viktor.aaalife.tests.api.v1.ping;

import com.viktor.aaalife.setup.api.v1.base.ApiBaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PingTest extends ApiBaseTest {

    @Test
    public void ping_healthCheck_returnsSuccessStatus() {
        Response response = client().ping();

        // doc says https://www.kuapp.com/apidoc#api-Ping-Ping
        // Success 200
        // BUT
        //HTTP/1.1 201 Created

        assertThat(response.statusCode()).isIn(200, 201);

    }
}