package com.viktor.aaalife.tests.api.v1.booking;

import com.viktor.aaalife.setup.api.v1.base.ApiBaseTest;
import com.viktor.aaalife.setup.api.v1.models.Booking;
import com.viktor.aaalife.setup.api.v1.models.BookingDates;
import com.viktor.aaalife.setup.api.v1.utils.BookingDataProvider;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteBookingTests extends ApiBaseTest {

    @Test
    public void deleteBooking_existingBookingWithValidToken_removesBooking() {
        Booking booking = new Booking(
                "Iron",
                "Man",
                250,
                true,
                new BookingDates("2018-01-01", "2019-01-01"),
                "Breakfast"
        );

        Response createResponse = client().createBooking(booking);
        assertThat(createResponse.statusCode()).isEqualTo(200);

        int bookingId = createResponse.jsonPath().getInt("bookingid");
        assertThat(bookingId)
                .as("Created booking id should be positive")
                .isGreaterThan(0);

        String token = client().createTokenValue("admin", "password123");
        // https://restful-booker.herokuapp.com/apidoc/index.html#api-Booking-DeleteBooking
        // doc says Success 200
        // BUT
        // HTTP/1.1 201 Created
        assertThat(client().deleteBooking(bookingId, token).statusCode()).isIn(200, 201);

        // this response code is not defined, but according to best practices not found is the best response
        assertThat(client().getBookingById(bookingId).statusCode())
                .as("Deleted booking should not be available anymore")
                .isEqualTo(404);
    }

    @Test(dataProvider = "invalidDeleteBookingData", dataProviderClass = BookingDataProvider.class)
    public void deleteBooking_invalidOrBoundaryBookingId_returnsExpectedError(
            Object bookingIdInput,
            String tokenInput,
            List<Integer> expectedStatuses,
            String caseName
    ) {
        Object bookingId = bookingIdInput;
        String token = resolveToken(tokenInput);

        if ("CREATED_BOOKING".equals(bookingIdInput)) {
            bookingId = createBookingAndReturnId();
        }

        Response response = client().deleteBookingRaw(bookingId, token);

        assertThat(response.statusCode())
                .as(caseName)
                .isIn(expectedStatuses.toArray());
    }

    @Test(dataProvider = "invalidDeleteAuthData", dataProviderClass = BookingDataProvider.class)
    public void deleteBooking_invalidOrMissingToken_returnsForbidden(
            String token,
            List<Integer> expectedStatuses,
            String caseName) {


        Response response = client().deleteBookingRaw(createBookingAndReturnId(), token);

        assertThat(response.statusCode())
                .as(caseName)
                .isIn(expectedStatuses.toArray());
    }

    private int createBookingAndReturnId() {
        Response createResponse = client().createBooking(defaultBooking());
        assertThat(createResponse.statusCode()).isEqualTo(200);

        int bookingId = createResponse.jsonPath().getInt("bookingid");
        assertThat(bookingId)
                .as("Created booking id should be positive")
                .isGreaterThan(0);

        return bookingId;
    }

    private Booking defaultBooking() {
        return new Booking(
                "Iron",
                "Man",
                250,
                true,
                new BookingDates("2018-01-01", "2019-01-01"),
                "Breakfast"
        );
    }

    private String resolveToken(String tokenInput) {
        if ("VALID_TOKEN".equals(tokenInput)) {
            return client().createTokenValue("admin", "password123");
        }

        if ("INVALID_TOKEN".equals(tokenInput)) {
            return "invalid-token";
        }

        return tokenInput; // allows null = missing token
    }
}