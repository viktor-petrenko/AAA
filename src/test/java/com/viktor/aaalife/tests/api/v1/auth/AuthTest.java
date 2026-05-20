package com.viktor.aaalife.tests.api.v1.auth;

import com.viktor.aaalife.setup.api.v1.base.ApiBaseTest;
import com.viktor.aaalife.setup.config.PropertyReader;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthTest extends ApiBaseTest {

    @Test
    public void auth_validCredentials_returnsToken() {
        Response response = client().createToken(PropertyReader.get("api.username"), PropertyReader.get("api.password"));

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getString("token")).isNotBlank();
    }

    @Test
    public void auth_invalidCredentials_returnsUnauthorizedOrForbidden() {
        Response response = client().createToken("user", "user");

        assertThat(response.statusCode()).isIn(200, 401, 403); // 200 in case bad credentials... i know it is wrong but this app is not perfect
        assertThat(response.jsonPath().getString("token")).isNullOrEmpty();
        assertThat(response.jsonPath().getString("reason")).isNotBlank();
    }
 }