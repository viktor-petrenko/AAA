package com.viktor.aaalife.tests.api.v1.contract.createbooking;

import com.viktor.aaalife.setup.api.v1.base.ApiBaseTest;
import com.viktor.aaalife.setup.api.v1.models.Booking;
import com.viktor.aaalife.setup.api.v1.utils.BookingDataProvider;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.viktor.aaalife.setup.api.v1.utils.SchemaValidator.assertMatchesSchema;
import static com.viktor.aaalife.setup.api.v1.utils.SchemaValidator.assertObjectMatchesSchema;
import static org.assertj.core.api.Assertions.assertThat;

public class SchemaTest extends ApiBaseTest {

    private static final String CREATE_BOOKING_REQUEST_SCHEMA =
            "testdata/api/v1/schemas/create-booking-request-schema.json";

    private static final String CREATE_BOOKING_RESPONSE_SCHEMA =
            "testdata/api/v1/schemas/create-booking-response-schema.json";

    @Test(dataProvider = "bookingData", dataProviderClass = BookingDataProvider.class)
    public void shouldMatchCreateBookingRequestSchemaForValidTestData(Booking booking) {
        assertObjectMatchesSchema(booking, CREATE_BOOKING_REQUEST_SCHEMA);
    }

    @Test(dataProvider = "bookingData", dataProviderClass = BookingDataProvider.class)
    public void shouldMatchCreateBookingResponseSchemaForValidPayload(Booking booking) {
        Response response = client().createBooking(booking);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.contentType()).contains("application/json");
        assertMatchesSchema(response, CREATE_BOOKING_RESPONSE_SCHEMA);
    }
}
