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
import com.yapp.ndgl.clients.google.places.GoogleMapsPlaceDetailClient;
import com.yapp.ndgl.clients.google.places.dto.request.PlaceDetailsRequest;
import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceDetailsResponse;
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
		GooglePlaceDetailsResponse response = createSuccessResponse();
		mockWebServer.enqueue(new MockResponse()
			.setBody(objectMapper.writeValueAsString(response))
			.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

		PlaceDetailsRequest request = PlaceDetailsRequest.builder()
			.placeId("ChIJN1t_tDeuEmsRUsoyG83frY4")
			.build();

		// when
		GooglePlaceDetailsResponse result = googleMapsClient.readPlaceDetails(request);

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
		assertThat(result.photos()).hasSize(1);
		assertThat(result.photos().get(0).name()).isEqualTo("places/test/photos/1");
		assertThat(result.photos().get(0).widthPx()).isEqualTo(3024);
		assertThat(result.photos().get(0).heightPx()).isEqualTo(3024);
		assertThat(result.photos().get(0).flagContentUri()).isEqualTo("https://www.google.com/local/imagery/report/?image_key=test");
		assertThat(result.photos().get(0).googleMapsUri()).isEqualTo("https://www.google.com/maps/place/test");
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

	private GooglePlaceDetailsResponse createSuccessResponse() {
		GooglePlaceDetailsResponse.DisplayName displayName = new GooglePlaceDetailsResponse.DisplayName(
			"Test Place"
		);

		GooglePlaceDetailsResponse.Location location = new GooglePlaceDetailsResponse.Location(
			37.5665,
			126.9780
		);

		GooglePlaceDetailsResponse.RegularOpeningHours openingHours = new GooglePlaceDetailsResponse.RegularOpeningHours(
			List.of("월요일: 24시간 영업")
		);

		GooglePlaceDetailsResponse.PhotoMeta photo = new GooglePlaceDetailsResponse.PhotoMeta(
			"places/test/photos/1",
			3024,
			3024,
			"https://www.google.com/local/imagery/report/?image_key=test",
			"https://www.google.com/maps/place/test"
		);

		return new GooglePlaceDetailsResponse(
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
			List.of(photo)
		);
	}

}
