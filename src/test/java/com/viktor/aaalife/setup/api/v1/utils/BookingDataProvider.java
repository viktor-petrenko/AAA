package com.viktor.aaalife.setup.api.v1.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viktor.aaalife.setup.api.v1.models.Booking;
import com.viktor.aaalife.setup.api.v1.models.InvalidBookingCase;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class BookingDataProvider {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String VALID_BOOKING_DATA_PATH =
            "testdata/api/v1/booking/booking.json";

    private static final String INVALID_BOOKING_DATA_PATH =
            "testdata/api/v1/booking/invalid-bookings.json";

    private BookingDataProvider() {
    }

    @DataProvider(name = "bookingData")
    public static Object[][] bookingData() {
        List<Booking> bookings = readJson(
                VALID_BOOKING_DATA_PATH,
                new TypeReference<List<Booking>>() {
                }
        );

        return bookings.stream()
                .map(booking -> new Object[]{booking})
                .toArray(Object[][]::new);
    }

    @DataProvider(name = "invalidBookingData")
    public static Object[][] invalidBookingData() {
        List<InvalidBookingCase> cases = readJson(
                INVALID_BOOKING_DATA_PATH,
                new TypeReference<List<InvalidBookingCase>>() {
                }
        );

        return cases.stream()
                .map(testCase -> new Object[]{testCase})
                .toArray(Object[][]::new);
    }

    @DataProvider(name = "invalidDeleteBookingData")
    public static Object[][] invalidDeleteBookingData() {
        return new Object[][]{
                {0, "VALID_TOKEN", List.of(404, 405), "Boundary ID zero with valid token"},
                {-1, "VALID_TOKEN", List.of(404, 405), "Negative booking ID with valid token"},
                {Integer.MAX_VALUE, "VALID_TOKEN", List.of(404, 405), "Non-existing max integer ID with valid token"},
                {Long.MAX_VALUE, "VALID_TOKEN", List.of(404, 405), "Non-existing very large long ID with valid token"},
                {"abc", "VALID_TOKEN", List.of(400, 404, 405), "Non-numeric booking ID with valid token"},
                {"1.5", "VALID_TOKEN", List.of(400, 404, 405), "Decimal booking ID with valid token"},
                {"!@#$", "VALID_TOKEN", List.of(400, 404, 405), "Special characters booking ID with valid token"},
                {"CREATED_BOOKING", "INVALID_TOKEN", List.of(403), "Valid booking ID with invalid token"},
                {"CREATED_BOOKING", null, List.of(403), "Valid booking ID without token"}
        };
    }

    @DataProvider(name = "invalidDeleteAuthData")
    public static Object[][] invalidDeleteAuthData() {
        return new Object[][]{
                {"invalid-token", List.of(403), "Invalid token"},
                {null, List.of(403), "Missing token"}
        };
    }

    private static <T> T readJson(String path, TypeReference<T> typeReference) {
        try (InputStream inputStream = BookingDataProvider.class
                .getClassLoader()
                .getResourceAsStream(path)) {

            if (inputStream == null) {
                throw new IllegalArgumentException("Test data file not found: " + path);
            }

            return OBJECT_MAPPER.readValue(inputStream, typeReference);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read booking test data from: " + path, e);
        }
    }
}