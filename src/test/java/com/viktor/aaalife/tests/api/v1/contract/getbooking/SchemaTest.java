package com.viktor.aaalife.tests.api.v1.contract.getbooking;

import com.viktor.aaalife.setup.api.v1.base.ApiBaseTest;
import com.viktor.aaalife.setup.api.v1.models.Booking;
import com.viktor.aaalife.setup.api.v1.utils.BookingDataProvider;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.viktor.aaalife.setup.api.v1.utils.SchemaValidator.assertMatchesSchema;
import static com.viktor.aaalife.setup.api.v1.utils.SchemaValidator.assertObjectMatchesSchema;
import static org.assertj.core.api.Assertions.assertThat;

public class SchemaTest extends ApiBaseTest {

    @Test
    public void getBookingById_existingBooking_matchesBookingResponseSchema() {
        Booking booking = BookingDataProvider.defaultBooking();

        Response createResponse = client().createBooking(booking);

        assertThat(createResponse.statusCode())
                .as("Create booking request must succeed before GET schema validation")
                .isEqualTo(200);

        int bookingId = createResponse.jsonPath().getInt("bookingid");

        Response getResponse = client().getBookingById(bookingId);

        assertThat(getResponse.statusCode())
                .as("GET booking by valid ID should return 200")
                .isEqualTo(200);

        assertMatchesSchema(getResponse, "testdata/api/v1/schemas/get-booking-response-schema.json");
    }
}
