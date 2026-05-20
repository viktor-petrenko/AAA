package com.viktor.aaalife.tests.api.v1.contract.getbookingbyids;

import com.viktor.aaalife.setup.api.v1.base.ApiBaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static com.viktor.aaalife.setup.api.v1.utils.SchemaValidator.assertMatchesSchema;
import static org.assertj.core.api.Assertions.assertThat;

public class SchemaTest extends ApiBaseTest {

    @Test
    public void getBookingIds_response_matchesBookingIdsSchema() {
        Response response = client().getBookingIds();

        assertThat(response.statusCode()).isEqualTo(200);
        assertMatchesSchema(response, "testdata/api/v1/schemas/booking-ids-response-schema.json");
    }
}
