package com.viktor.aaalife.tests.api.v1.contract.updatebooking;

import com.viktor.aaalife.setup.api.v1.base.ApiBaseTest;
import com.viktor.aaalife.setup.api.v1.models.Booking;
import com.viktor.aaalife.setup.api.v1.models.BookingDates;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.viktor.aaalife.setup.api.v1.utils.SchemaValidator.assertMatchesSchema;
import static org.assertj.core.api.Assertions.assertThat;

public class SchemaTest extends ApiBaseTest {

    @Test
    public void verifyUpdateBookingSchema() {
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
        String token = client().createTokenValue("admin", "password123");

        Booking updatedBooking = new Booking(
                "Tony",
                "Stark",
                500,
                false,
                new BookingDates("2026-06-01", "2026-06-07"),
                "Dinner"
        );

        Response updateResponse = client().updateBooking(bookingId, updatedBooking, token);

        assertThat(updateResponse.statusCode()).isEqualTo(200);
        assertMatchesSchema(updateResponse, "testdata/api/v1/schemas/booking-response-schema.json");
    }
}
