package com.viktor.aaalife.tests.api.v1.booking;

import com.viktor.aaalife.setup.api.v1.base.ApiBaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingIdsTests extends ApiBaseTest {

    @Test
    public void shouldReturnBookingIdsWhenBookingsExist() {
        Response response = client().getBookingIds();

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body().asString()).isNotBlank();

        List<Integer> bookingIds = response.jsonPath().getList("bookingid", Integer.class);
        assertThat(bookingIds)
                .as("Booking IDs should be returned")
                .isNotEmpty()
                .allMatch(id -> id > 0);
    }
}
