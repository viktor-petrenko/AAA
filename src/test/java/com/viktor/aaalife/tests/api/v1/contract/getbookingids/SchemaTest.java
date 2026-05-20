package com.viktor.aaalife.tests.api.v1.contract.getbookingids;

import com.viktor.aaalife.setup.api.v1.base.ApiBaseTest;
import com.viktor.aaalife.setup.api.v1.models.Booking;
import com.viktor.aaalife.setup.api.v1.models.BookingDates;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.viktor.aaalife.setup.api.v1.utils.SchemaValidator.assertMatchesSchema;
import static org.assertj.core.api.Assertions.assertThat;

public class SchemaTest extends ApiBaseTest {

    @Test
    public void verifyGetBookingIdsSchema() {
        Response response = client().getBookingIds();

        assertThat(response.statusCode()).isEqualTo(200);
        assertMatchesSchema(response, "testdata/api/v1/schemas/booking-ids-response-schema.json");
    }
}
