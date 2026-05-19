package com.viktor.aaalife.setup.api.v1.clients;

import com.viktor.aaalife.setup.api.v1.configs.ApiConfig;
import com.viktor.aaalife.setup.api.v1.models.Booking;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class RestfulBookerClient {
    //TODO separate clients? brakedown into contexts?

    public Response createToken(String username, String password) {
        Map<String, String> credentials = Map.of(
                "username", username,
                "password", password
        );

        Response response = request()
                .body(credentials)
                .when()
                .post("/auth");

        return logResponseIfEnabled(response);
    }

    public String createTokenValue(String username, String password) {
        return createToken(username, password)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");
    }

    public Response getBookingIds() {
        Response response = request()
                .when()
                .get("/booking");

        return logResponseIfEnabled(response);
    }

    public Response getBookingById(int bookingId) {
        return getBookingByIdRaw(bookingId);
    }

    public Response getBookingByIdRaw(Object bookingId) {
        Response response = request()
                .pathParam("id", bookingId)
                .when()
                .get("/booking/{id}");

        return logResponseIfEnabled(response);
    }

    public Response createBooking(Booking booking) {
        Response response = request()
                .body(booking)
                .when()
                .post("/booking");

        return logResponseIfEnabled(response);
    }

    public Response createBooking(Map<String, Object> payload) {
        Response response = request()
                .body(payload)
                .when()
                .post("/booking");

        return logResponseIfEnabled(response);
    }

    public Response searchBookingByName(String firstname, String lastname) {
        Response response = request()
                .queryParam("firstname", firstname)
                .queryParam("lastname", lastname)
                .when()
                .get("/booking");

        return logResponseIfEnabled(response);
    }

    public Response updateBooking(int bookingId, Booking booking, String token) {
        Response response = requestWithToken(token)
                .pathParam("id", bookingId)
                .body(booking)
                .when()
                .put("/booking/{id}");

        return logResponseIfEnabled(response);
    }

    public Response partialUpdateBooking(int bookingId, Map<String, Object> partialPayload, String token) {
        Response response = requestWithToken(token)
                .pathParam("id", bookingId)
                .body(partialPayload)
                .when()
                .patch("/booking/{id}");

        return logResponseIfEnabled(response);
    }

    public Response deleteBooking(int bookingId, String token) {
        return deleteBookingRaw(bookingId, token);
    }

    public Response deleteBookingRaw(Object bookingId, String token) {
        RequestSpecification request = token == null
                ? request()
                : requestWithToken(token);

        Response response = request
                .pathParam("id", String.valueOf(bookingId))
                .when()
                .delete("/booking/{id}");

        return logResponseIfEnabled(response);
    }

    public Response ping() {
        Response response = request()
                .when()
                .get("/ping");

        return logResponseIfEnabled(response);
    }

    // ------------ helpers

    private RequestSpecification request() {
        RequestSpecification request = given()
                .spec(ApiConfig.defaultRequestSpec());

        if (isApiLoggingEnabled()) {
            request.log().all();
        }

        return request;
    }

    private RequestSpecification requestWithToken(String token) {
        return request()
                .cookie("token", token);
    }

    private Response logResponseIfEnabled(Response response) {
        if (isApiLoggingEnabled()) {
            response.then().log().all();
        }

        return response;
    }

    // TODO move to properties
    private boolean isApiLoggingEnabled() {
        return Boolean.parseBoolean(System.getProperty("api.logging", "true"));
    }
}