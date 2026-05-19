package com.viktor.aaalife.tests.api.v1.auth;

import com.viktor.aaalife.setup.api.v1.base.ApiBaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthTest extends ApiBaseTest {

    @Test
    public void successfulTokenCreation() {
        Response response = client().createToken("admin", "password123");

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getString("token")).isNotBlank();
    }

    @Test
    public void invalidTokenCreation() {
        Response response = client().createToken("user", "user");

        assertThat(response.statusCode()).isIn(401, 403);
        assertThat(response.jsonPath().getString("token")).isBlank();
    }

    // other tests
    // jwt..etcetc
}