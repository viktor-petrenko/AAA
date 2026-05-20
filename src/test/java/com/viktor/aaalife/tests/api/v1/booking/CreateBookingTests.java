package com.viktor.aaalife.tests.api.v1.booking;

import com.viktor.aaalife.setup.api.v1.base.ApiBaseTest;
import com.viktor.aaalife.setup.api.v1.models.Booking;
import com.viktor.aaalife.setup.api.v1.models.BookingDates;
import com.viktor.aaalife.setup.api.v1.models.InvalidBookingCase;
import com.viktor.aaalife.setup.api.v1.utils.BookingDataProvider;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class CreateBookingTests extends ApiBaseTest {

    @Test(dataProvider = "bookingData", dataProviderClass = BookingDataProvider.class)
    public void shouldCreateBookingWhenValidPayloadProvided(Booking booking) {
        Response response = client().createBooking(booking);

        assertSoftly(softly -> {

            softly.assertThat(response.statusCode())
                    .isEqualTo(200);

            softly.assertThat(response.jsonPath().getInt("bookingid"))
                    .isGreaterThan(0);

            softly.assertThat(response.jsonPath().getString("booking.firstname"))
                    .isEqualTo(booking.firstname());

            softly.assertThat(response.jsonPath().getString("booking.lastname"))
                    .isEqualTo(booking.lastname());

            softly.assertThat(response.jsonPath().getInt("booking.totalprice"))
                    .isEqualTo(booking.totalprice());

            softly.assertThat(response.jsonPath().getBoolean("booking.depositpaid"))
                    .isEqualTo(booking.depositpaid());

            softly.assertThat(response.jsonPath().getString("booking.bookingdates.checkin"))
                    .isEqualTo(booking.bookingdates().checkin());

            softly.assertThat(response.jsonPath().getString("booking.bookingdates.checkout"))
                    .isEqualTo(booking.bookingdates().checkout());

            softly.assertThat(response.jsonPath().getString("booking.additionalneeds"))
                    .isEqualTo(booking.additionalneeds());

        });
    }

    @Test(dataProvider = "invalidBookingData", dataProviderClass = BookingDataProvider.class)
    public void shouldRejectBookingWhenInvalidPayloadProvided(InvalidBookingCase testCase) {
        Response response = client().createBooking(testCase.payload());

        assertThat(response.statusCode())
                .as(testCase.caseName())
                .isIn(testCase.expectedStatuses().toArray()); // For Restful Booker, I would allow [400, 500] because the API is not very strict/consistent and is down with validation errors. For a real production API, I would expect 400
    }

    /**
     * minimum valid totalprice = 1 or 1-character name
     */
    @Test
    public void shouldCreateBookingWhenMinimumBoundaryValuesProvided() {
        Booking booking = new Booking(
                "A",
                "B",
                1,
                true,
                new BookingDates("2026-06-01", "2026-06-02"),
                "None"
        );

        Response response = client().createBooking(booking);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().getInt("bookingid")).isGreaterThan(0);
        assertThat(response.jsonPath().getInt("booking.totalprice")).isEqualTo(1);
        assertThat(response.jsonPath().getString("booking.firstname")).isEqualTo("A");
        assertThat(response.jsonPath().getString("booking.lastname")).isEqualTo("B");
    }

}