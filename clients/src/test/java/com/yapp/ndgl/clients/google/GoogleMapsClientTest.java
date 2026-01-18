package com.yapp.ndgl.clients.google;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.clients.google.config.GoogleMapsPlaceApiConfig;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlaceDetailClient;
import com.yapp.ndgl.clients.google.places.dto.request.PlaceDetailsRequest;
import com.yapp.ndgl.clients.google.places.dto.response.PlaceDetailsResponse;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.GoogleMapsErrorCode;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

@DisplayName("GoogleMapsClient 테스트")
class GoogleMapsClientTest {

	private MockWebServer mockWebServer;
	private GoogleMapsPlaceDetailClient googleMapsClient;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();

		RestClient restClient = RestClient.builder()
			.baseUrl(mockWebServer.url("/").toString())
			.build();

		googleMapsClient = new GoogleMapsPlaceDetailClient(restClient);
		objectMapper = new ObjectMapper();
	}

	@AfterEach
	void tearDown() throws IOException {
		mockWebServer.shutdown();
	}

	@Test
	@DisplayName("Place Details API 호출 성공 시 장소 정보를 반환한다")
	void getPlaceDetails_success() throws JsonProcessingException {
		// given
		PlaceDetailsResponse response = createSuccessResponse();
		mockWebServer.enqueue(new MockResponse()
			.setBody(objectMapper.writeValueAsString(response))
			.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

		PlaceDetailsRequest request = PlaceDetailsRequest.builder()
			.placeId("ChIJN1t_tDeuEmsRUsoyG83frY4")
			.build();

		// when
		PlaceDetailsResponse result = googleMapsClient.readPlaceDetails(request);

		// then
		assertThat(result.id()).isEqualTo("ChIJN1t_tDeuEmsRUsoyG83frY4");
		assertThat(result.displayName()).isNotNull();
		assertThat(result.displayName().text()).isEqualTo("Test Place");
		assertThat(result.formattedAddress()).isEqualTo("123 Test Street, Test City");
		assertThat(result.nationalPhoneNumber()).isEqualTo("050-0000-0000");
		assertThat(result.internationalPhoneNumber()).isEqualTo("+81 50-0000-0000");
		assertThat(result.rating()).isEqualTo(4.5);
		assertThat(result.googleMapsUri()).isEqualTo("https://maps.google.com/?cid=test");
		assertThat(result.websiteUri()).isEqualTo("https://example.com");
		assertThat(result.userRatingCount()).isEqualTo(100);
		assertThat(result.location()).isNotNull();
		assertThat(result.location().latitude()).isEqualTo(37.5665);
		assertThat(result.location().longitude()).isEqualTo(126.9780);
		assertThat(result.regularOpeningHours()).isNotNull();
		assertThat(result.regularOpeningHours().openNow()).isTrue();
		assertThat(result.regularOpeningHours().periods()).hasSize(1);
		assertThat(result.regularOpeningHours().periods().get(0).open().day()).isEqualTo(0);
		assertThat(result.regularOpeningHours().periods().get(0).open().hour()).isEqualTo(0);
		assertThat(result.regularOpeningHours().periods().get(0).open().minute()).isEqualTo(0);
		assertThat(result.photos()).hasSize(1);
		assertThat(result.photos().get(0).name()).isEqualTo("places/test/photos/1");
		assertThat(result.photos().get(0).widthPx()).isEqualTo(3024);
		assertThat(result.photos().get(0).heightPx()).isEqualTo(3024);
		assertThat(result.photos().get(0).authorAttributions()).hasSize(1);
		assertThat(result.photos().get(0).authorAttributions().get(0).displayName()).isEqualTo("Y.Maruyama");
		assertThat(result.photos().get(0).flagContentUri()).isEqualTo("https://www.google.com/local/imagery/report/?image_key=test");
		assertThat(result.photos().get(0).googleMapsUri()).isEqualTo("https://www.google.com/maps/place/test");
		assertThat(result.postalAddress()).isNotNull();
		assertThat(result.postalAddress().regionCode()).isEqualTo("JP");
		assertThat(result.postalAddress().languageCode()).isEqualTo("ko");
		assertThat(result.postalAddress().postalCode()).isEqualTo("810-0801");
		assertThat(result.postalAddress().administrativeArea()).isEqualTo("후쿠오카현");
		assertThat(result.postalAddress().addressLines()).containsExactly("5 조메-3-2 나카스", "하카타구 후쿠오카시");
	}

	@Test
	@DisplayName("HTTP 오류 응답 시 API_CALL_FAILED 예외를 던진다")
	void getPlaceDetails_httpError() {
		// given
		mockWebServer.enqueue(new MockResponse()
			.setResponseCode(500)
			.setBody("Internal Server Error"));

		PlaceDetailsRequest request = PlaceDetailsRequest.builder()
			.placeId("ChIJN1t_tDeuEmsRUsoyG83frY4")
			.build();

		// when & then
		assertThatThrownBy(() -> googleMapsClient.readPlaceDetails(request))
			.isInstanceOf(GlobalException.class)
			.satisfies(exception -> {
				GlobalException globalException = (GlobalException) exception;
				assertThat(globalException.getBaseErrorCode()).isEqualTo(GoogleMapsErrorCode.API_CALL_FAILED);
			});
	}

	private PlaceDetailsResponse createSuccessResponse() {
		PlaceDetailsResponse.DisplayName displayName = new PlaceDetailsResponse.DisplayName(
			"Test Place",
			"en"
		);

		PlaceDetailsResponse.Location location = new PlaceDetailsResponse.Location(
			37.5665,
			126.9780
		);

		PlaceDetailsResponse.DayTime openTime = new PlaceDetailsResponse.DayTime(
			0,
			0,
			0
		);

		PlaceDetailsResponse.Period period = new PlaceDetailsResponse.Period(
			openTime,
			null
		);

		PlaceDetailsResponse.RegularOpeningHours openingHours = new PlaceDetailsResponse.RegularOpeningHours(
			true,
			List.of(period),
			List.of("월요일: 24시간 영업")
		);

		PlaceDetailsResponse.AuthorAttribution attribution = new PlaceDetailsResponse.AuthorAttribution(
			"Y.Maruyama",
			"https://maps.google.com/maps/contrib/110073645762581158013",
			"https://lh3.googleusercontent.com/a-/test"
		);

		PlaceDetailsResponse.Photo photo = new PlaceDetailsResponse.Photo(
			"places/test/photos/1",
			3024,
			3024,
			List.of(attribution),
			"https://www.google.com/local/imagery/report/?image_key=test",
			"https://www.google.com/maps/place/test"
		);

		PlaceDetailsResponse.PostalAddress postalAddress = new PlaceDetailsResponse.PostalAddress(
			"JP",
			"ko",
			"810-0801",
			"후쿠오카현",
			List.of("5 조메-3-2 나카스", "하카타구 후쿠오카시")
		);

		return new PlaceDetailsResponse(
			"ChIJN1t_tDeuEmsRUsoyG83frY4",
			"050-0000-0000",
			"+81 50-0000-0000",
			"123 Test Street, Test City",
			location,
			4.5,
			"https://maps.google.com/?cid=test",
			"https://example.com",
			openingHours,
			100,
			displayName,
			List.of(photo),
			postalAddress
		);
	}

	private static class TestGoogleMapsConfig extends GoogleMapsPlaceApiConfig {
		private final String testApiKey;

		TestGoogleMapsConfig(final String testApiKey) {
			this.testApiKey = testApiKey;
		}

		@Override
		public String getApiKey() {
			return testApiKey;
		}
	}
}
