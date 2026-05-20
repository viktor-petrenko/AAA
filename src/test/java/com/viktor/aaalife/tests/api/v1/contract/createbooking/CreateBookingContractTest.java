package com.viktor.aaalife.tests.api.v1.contract.createbooking;

import com.viktor.aaalife.setup.api.v1.base.ApiBaseTest;
import com.viktor.aaalife.setup.api.v1.models.Booking;
import com.viktor.aaalife.setup.api.v1.models.BookingDates;
import com.viktor.aaalife.setup.api.v1.models.InvalidBookingCase;
import com.viktor.aaalife.setup.api.v1.utils.BookingDataProvider;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Set;

import static com.viktor.aaalife.setup.api.v1.utils.SchemaValidator.assertMatchesSchema;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class CreateBookingContractTest extends ApiBaseTest {

    private static final String CREATE_BOOKING_RESPONSE_SCHEMA = "testdata/api/v1/schemas/create-booking-response-schema.json";
    private static final String GET_BOOKING_BY_ID_RESPONSE_SCHEMA = "testdata/api/v1/schemas/getbookingby-id-response-schema.json";

    @Test(dataProvider = "bookingData", dataProviderClass = BookingDataProvider.class)
    public void createBooking_validPayload_matchesDocumentedResponseContract(Booking booking) {
        Response response = client().createBooking(booking);

        assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(200);
            softly.assertThat(response.contentType()).contains("application/json");
        });

        assertMatchesSchema(response, CREATE_BOOKING_RESPONSE_SCHEMA);
        assertCreateBookingResponseContract(response, booking);
    }

    @Test
    public void createBooking_returnedBookingId_retrievesPersistedBooking() {
        Booking booking = defaultBooking();

        Response createResponse = client().createBooking(booking);
        assertThat(createResponse.statusCode()).isEqualTo(200);
        assertMatchesSchema(createResponse, CREATE_BOOKING_RESPONSE_SCHEMA);

        int bookingId = createResponse.jsonPath().getInt("bookingid");
        assertThat(bookingId).isGreaterThan(0);

        Response getResponse = client().getBookingById(bookingId);

        assertSoftly(softly -> {
            softly.assertThat(getResponse.statusCode()).isEqualTo(200);
            softly.assertThat(getResponse.contentType()).contains("application/json");
        });

        assertMatchesSchema(getResponse, GET_BOOKING_BY_ID_RESPONSE_SCHEMA);
        Map<String, Object> retrievedBooking = getResponse.as(new TypeRef<>() {});
        assertBookingPayloadMatches(retrievedBooking, booking);
    }

    @Test
    public void createBooking_boundaryPayload_matchesResponseContract() {
        Booking booking = new Booking(
                "A",
                "B",
                1,
                false,
                new BookingDates("2026-01-01", "2026-01-02"),
                "None"
        );

        Response response = client().createBooking(booking);

        assertThat(response.statusCode()).isEqualTo(200);
        assertMatchesSchema(response, CREATE_BOOKING_RESPONSE_SCHEMA);
        assertCreateBookingResponseContract(response, booking);
    }

    @Test(dataProvider = "invalidBookingData", dataProviderClass = BookingDataProvider.class)
    public void createBooking_payloadBreakingRequiredContract_returnsNonSuccess(InvalidBookingCase testCase) {
        Response response = client().createBooking(testCase.payload());

        // Restful Booker is intentionally buggy and sometimes returns 500 instead of a clean 400.
        // The contract expectation for this test suite is: invalid payload must not return 2xx.
        assertThat(response.statusCode())
                .as(testCase.caseName())
                .isIn(testCase.expectedStatuses().toArray());
    }

    @Test
    public void createBooking_malformedJsonBody_returnsNonSuccess() {
        Response response = client().createBookingRaw("{ \"firstname\": \"Broken\", ");

        assertThat(response.statusCode())
                .as("Malformed JSON must not be accepted as a created booking")
                .isIn(400, 500);
    }

    @SuppressWarnings("unchecked")
    private void assertCreateBookingResponseContract(Response response, Booking expectedBooking) {
        Map<String, Object> responseBody = response.as(new TypeRef<>() {
        });

        assertSoftly(softly -> {
            softly.assertThat(responseBody.keySet())
                    .as("Create booking response should contain only documented top-level fields")
                    .containsExactlyInAnyOrderElementsOf(Set.of("bookingid", "booking"));

            softly.assertThat(responseBody.get("bookingid"))
                    .as("bookingid should be returned as an integer")
                    .isInstanceOf(Integer.class);

            softly.assertThat((Integer) responseBody.get("bookingid"))
                    .as("bookingid should be positive")
                    .isGreaterThan(0);

            softly.assertThat(responseBody.get("booking"))
                    .as("booking should be returned as nested object")
                    .isInstanceOf(Map.class);
        });

        Map<String, Object> bookingBody = (Map<String, Object>) responseBody.get("booking");
        assertBookingPayloadMatches(bookingBody, expectedBooking);
    }

    @SuppressWarnings("unchecked")
    private void assertBookingPayloadMatches(Map<String, Object> actualBooking, Booking expectedBooking) {
        assertSoftly(softly -> {
            softly.assertThat(actualBooking.keySet())
                    .as("Booking object should contain only documented fields")
                    .containsExactlyInAnyOrderElementsOf(Set.of(
                            "firstname",
                            "lastname",
                            "totalprice",
                            "depositpaid",
                            "bookingdates",
                            "additionalneeds"
                    ));

            softly.assertThat(actualBooking.get("firstname")).isEqualTo(expectedBooking.firstname());
            softly.assertThat(actualBooking.get("lastname")).isEqualTo(expectedBooking.lastname());
            softly.assertThat(actualBooking.get("totalprice")).isEqualTo(expectedBooking.totalprice());
            softly.assertThat(actualBooking.get("depositpaid")).isEqualTo(expectedBooking.depositpaid());
            softly.assertThat(actualBooking.get("additionalneeds")).isEqualTo(expectedBooking.additionalneeds());
            softly.assertThat(actualBooking.get("bookingdates")).isInstanceOf(Map.class);
        });

        Map<String, Object> actualDates = (Map<String, Object>) actualBooking.get("bookingdates");

        assertSoftly(softly -> {
            softly.assertThat(actualDates.keySet())
                    .as("bookingdates should contain only checkin and checkout")
                    .containsExactlyInAnyOrderElementsOf(Set.of("checkin", "checkout"));

            softly.assertThat(actualDates.get("checkin")).isEqualTo(expectedBooking.bookingdates().checkin());
            softly.assertThat(actualDates.get("checkout")).isEqualTo(expectedBooking.bookingdates().checkout());
        });
    }

    private Booking defaultBooking() {
        return new Booking(
                "Tony",
                "Stark",
                500,
                true,
                new BookingDates("2026-06-01", "2026-06-07"),
                "Breakfast"
        );
    }
}
