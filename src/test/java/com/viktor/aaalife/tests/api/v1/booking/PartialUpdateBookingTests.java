package com.viktor.aaalife.tests.api.v1.booking;

import com.viktor.aaalife.setup.api.v1.base.ApiBaseTest;
import com.viktor.aaalife.setup.api.v1.models.Booking;
import com.viktor.aaalife.setup.api.v1.models.BookingDates;
import com.viktor.aaalife.setup.config.PropertyReader;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class PartialUpdateBookingTests extends ApiBaseTest {

    @Test
    public void partialUpdateBooking_selectedFieldsWithValidToken_updatesOnlyProvidedFields() {
        Booking originalBooking = new Booking(
                "Steve",
                "Rogers",
                300,
                true,
                new BookingDates("2018-01-01", "2019-01-01"),
                "Breakfast"
        );

        Response createResponse = client().createBooking(originalBooking);
        assertThat(createResponse.statusCode()).isEqualTo(200);

        int bookingId = createResponse.jsonPath().getInt("bookingid");

        assertThat(bookingId)
                .as("Created booking id should be positive")
                .isGreaterThan(0);

        Map<String, Object> partialPayload = Map.of(
                "firstname", "Captain",
                "lastname", "America"
        );

        String token = client().createTokenValue(PropertyReader.get("api.username"),  PropertyReader.get("api.password"));
        Response patchResponse = client().partialUpdateBooking(bookingId, partialPayload, token);

        assertThat(patchResponse.statusCode()).isEqualTo(200);
        assertThat(patchResponse.jsonPath().getString("firstname")).isEqualTo("Captain");
        assertThat(patchResponse.jsonPath().getString("lastname")).isEqualTo("America");

        // unchanged fields should remain from the original booking
        assertThat(patchResponse.jsonPath().getInt("totalprice")).isEqualTo(300);
        assertThat(patchResponse.jsonPath().getBoolean("depositpaid")).isTrue();
        assertThat(patchResponse.jsonPath().getString("bookingdates.checkin")).isEqualTo("2018-01-01");
        assertThat(patchResponse.jsonPath().getString("bookingdates.checkout")).isEqualTo("2019-01-01");
        assertThat(patchResponse.jsonPath().getString("additionalneeds")).isEqualTo("Breakfast");

        Response getResponse = client().getBookingById(bookingId);
        assertThat(getResponse.statusCode()).isEqualTo(200);
        assertThat(getResponse.jsonPath().getString("firstname")).isEqualTo("Captain");
        assertThat(getResponse.jsonPath().getString("lastname")).isEqualTo("America");
        assertThat(getResponse.jsonPath().getInt("totalprice")).isEqualTo(300);
        assertThat(getResponse.jsonPath().getString("additionalneeds")).isEqualTo("Breakfast");
    }
}