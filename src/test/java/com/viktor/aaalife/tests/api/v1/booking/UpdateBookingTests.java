package com.viktor.aaalife.tests.api.v1.booking;

import com.viktor.aaalife.setup.api.v1.base.ApiBaseTest;
import com.viktor.aaalife.setup.api.v1.models.Booking;
import com.viktor.aaalife.setup.api.v1.models.BookingDates;
import com.viktor.aaalife.setup.config.PropertyReader;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateBookingTests extends ApiBaseTest {

    @Test
    public void updateBooking_validPayloadAndToken_replacesBooking() {
        Booking originalBooking = new Booking(
                "Iron",
                "Man",
                250,
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

        Booking updatedBooking = new Booking(
                "Tony",
                "Stark",
                500,
                false,
                new BookingDates("2026-06-01", "2026-06-07"),
                "Dinner"
        );

        String token = client().createTokenValue(PropertyReader.get("api.username"), PropertyReader.get("api.password"));
        Response updateResponse = client().updateBooking(bookingId, updatedBooking, token);
        assertThat(updateResponse.statusCode()).isEqualTo(200);
        assertThat(updateResponse.jsonPath().getString("firstname")).isEqualTo("Tony");
        assertThat(updateResponse.jsonPath().getString("lastname")).isEqualTo("Stark");
        assertThat(updateResponse.jsonPath().getInt("totalprice")).isEqualTo(500);
        assertThat(updateResponse.jsonPath().getBoolean("depositpaid")).isFalse();
        assertThat(updateResponse.jsonPath().getString("bookingdates.checkin")).isEqualTo("2026-06-01");
        assertThat(updateResponse.jsonPath().getString("bookingdates.checkout")).isEqualTo("2026-06-07");
        assertThat(updateResponse.jsonPath().getString("additionalneeds")).isEqualTo("Dinner");

        //make sure.
        Response getResponse = client().getBookingById(bookingId);
        assertThat(getResponse.statusCode()).isEqualTo(200);
        assertThat(getResponse.jsonPath().getString("firstname")).isEqualTo("Tony");
        assertThat(getResponse.jsonPath().getString("lastname")).isEqualTo("Stark");
        assertThat(getResponse.jsonPath().getInt("totalprice")).isEqualTo(500);
        assertThat(getResponse.jsonPath().getBoolean("depositpaid")).isFalse();
        assertThat(getResponse.jsonPath().getString("additionalneeds")).isEqualTo("Dinner");
    }

}