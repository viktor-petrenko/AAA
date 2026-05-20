package com.viktor.aaalife.tests.api.v1.booking;

import com.viktor.aaalife.setup.api.v1.base.ApiBaseTest;
import com.viktor.aaalife.setup.api.v1.models.Booking;
import com.viktor.aaalife.setup.api.v1.utils.BookingDataProvider;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingTests extends ApiBaseTest {

    @Test(dataProvider = "bookingData", dataProviderClass = BookingDataProvider.class)
    public void getBookingById_existingBooking_returnsBookingDetails(Booking booking) {
        Response createResponse = client().createBooking(booking);
        assertThat(createResponse.statusCode()).isEqualTo(200);

        int bookingId = createResponse.jsonPath().getInt("bookingid");
        assertThat(bookingId)
                .as("Created booking id should be positive")
                .isGreaterThan(0);

        Response getResponse = client().getBookingById(bookingId);
        assertThat(getResponse.statusCode()).isEqualTo(200);
        assertThat(getResponse.jsonPath().getString("firstname"))
                .isEqualTo(booking.firstname());
        assertThat(getResponse.jsonPath().getString("lastname"))
                .isEqualTo(booking.lastname());
        assertThat(getResponse.jsonPath().getInt("totalprice"))
                .isEqualTo(booking.totalprice());
        assertThat(getResponse.jsonPath().getBoolean("depositpaid"))
                .isEqualTo(booking.depositpaid());
        assertThat(getResponse.jsonPath().getString("bookingdates.checkin"))
                .isEqualTo(booking.bookingdates().checkin());
        assertThat(getResponse.jsonPath().getString("bookingdates.checkout"))
                .isEqualTo(booking.bookingdates().checkout());
        assertThat(getResponse.jsonPath().getString("additionalneeds"))
                .isEqualTo(booking.additionalneeds());
    }


    @Test
    public void getBookingById_literalNullPathValue_returnsNotFound() {
        Response response = client().getBookingByIdRaw("null");

        assertThat(response.statusCode())
                .as("Non-existing booking ID should return 404")
                .isEqualTo(404);
    }

    @Test(dataProvider = "invalidBookingIds", dataProviderClass = BookingDataProvider.class)
    public void getBookingById_invalidOrBoundaryId_returnsExpectedError(
            Object bookingId,
            List<Integer> expectedStatuses,
            String caseName
    ) {
        Response response = client().getBookingByIdRaw(bookingId);

        assertThat(response.statusCode())
                .as(caseName)
                .isIn(expectedStatuses.toArray());
    }
}