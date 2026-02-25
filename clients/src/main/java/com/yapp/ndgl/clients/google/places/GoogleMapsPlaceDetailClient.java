package com.yapp.ndgl.clients.google.places;

import java.net.SocketTimeoutException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import com.yapp.ndgl.clients.google.places.dto.request.PlaceDetailsRequest;
import com.yapp.ndgl.clients.google.places.dto.request.PlaceNearbySearchRequest;
import com.yapp.ndgl.clients.google.places.dto.request.PlaceTextSearchRequest;
import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceDetailsResponse;
import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceNearbySearchResponse;
import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceTextSearchResponse;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.GoogleMapsErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring RestClient로 Google Maps Place Details API를 호출하는 클라이언트.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleMapsPlaceDetailClient {

	@Value("${google.maps.api-key}")
	private String apiKey;
	private static final String GOOGLE_MAPS_KEY_HEADER = "X-Goog-Api-Key";
	private static final String FIELD_MASK_HEADER = "X-Goog-FieldMask";
	private static final String TEXT_SEARCH_FIELD_MASK = "places.id,places.displayName";
	private static final String NEARBY_SEARCH_FIELD_MASK = String.join(",",
		"places.id",
		"places.displayName",
		"places.rating",
		"places.photos",
		"places.location",
		"places.websiteUri",
		"places.googleMapsUri",
		"places.formattedAddress",
		"places.nationalPhoneNumber",
		"places.internationalPhoneNumber",
		"places.regularOpeningHours",
		"places.userRatingCount",
		"places.primaryType",
		"places.types",
		"places.priceRange"
	);
	private static final String DEFAULT_FIELD_MASK = String.join(",",
		"displayName",
		"id",
		"rating",
		"photos",
		"location",
		"websiteUri",
		"googleMapsUri",
		"formattedAddress",
		"postalAddress",
		"nationalPhoneNumber",
		"internationalPhoneNumber",
		"regularOpeningHours",
		"userRatingCount",
		"primaryType",
		"types",
		"priceRange"
	);

	private final RestClient googleMapsPlaceRestClient;

	/**
	 * Place Details API를 호출하고 응답 상태를 검증한다.
	 *
	 * @param request Place Details API 요청 파라미터
	 * @return 검증된 Place Details 응답
	 */
	public GooglePlaceDetailsResponse readPlaceDetails(final PlaceDetailsRequest request) {
		try {
			validateRequest(request);

			final String uri = "/places/" + request.googlePlaceId();

			RestClient.ResponseSpec spec = googleMapsPlaceRestClient.get()
				.uri(uriBuilder -> {
					final URI requestURI = uriBuilder
						.path(uri)
						.queryParam("languageCode", request.language())
						.build();

					log.info("API 요청 URI = {}", requestURI);
					return requestURI;
				})
				.header(GOOGLE_MAPS_KEY_HEADER, apiKey)
				.header(FIELD_MASK_HEADER, DEFAULT_FIELD_MASK)
				.retrieve()
				.onStatus(HttpStatusCode::isError, (req, res) -> {
					log.error("Google Maps Place API 응답 오류 - 장소 세부 정보 조회시 오류 발생 (status={})", res.getStatusCode());
					throw new GlobalException(GoogleMapsErrorCode.API_CALL_FAILED);
				});

			final GooglePlaceDetailsResponse response = spec.body(GooglePlaceDetailsResponse.class);
			log.debug("Google Maps Place Details API 호출 성공: googlePlaceId={}", request.googlePlaceId());
			validateResponse(response);

			return response;

		} catch (ResourceAccessException e) {
			log.error("Google Maps API 요청 실패: {}", e.getMessage(), e);
			if (e.getCause() instanceof SocketTimeoutException) {
				throw new GlobalException(GoogleMapsErrorCode.API_TIMEOUT);
			}
			throw new GlobalException(GoogleMapsErrorCode.API_CALL_FAILED);
		}
	}

	public GooglePlaceTextSearchResponse searchPlacesByText(final PlaceTextSearchRequest request) {
		try {
			if (request == null || !StringUtils.hasText(request.textQuery())) {
				throw new GlobalException(GoogleMapsErrorCode.INVALID_PLACE_ID);
			}

			log.info("Google Maps Text Search API 호출: textQuery={}", request.textQuery());

			final GooglePlaceTextSearchResponse response = googleMapsPlaceRestClient.post()
				.uri("/places:searchText")
				.header(GOOGLE_MAPS_KEY_HEADER, apiKey)
				.header(FIELD_MASK_HEADER, TEXT_SEARCH_FIELD_MASK)
				.contentType(org.springframework.http.MediaType.APPLICATION_JSON)
				.body(request)
				.retrieve()
				.onStatus(HttpStatusCode::isError, (req, res) -> {
					log.error("Google Maps Text Search API 응답 오류 (status={})", res.getStatusCode());
					throw new GlobalException(GoogleMapsErrorCode.API_CALL_FAILED);
				})
				.body(GooglePlaceTextSearchResponse.class);


			if (response == null) {
				throw new GlobalException(GoogleMapsErrorCode.API_CALL_FAILED);
			}

			log.info("Google Maps Text Search 결과 = {}", response.places().get(0).displayName());

			return response;

		} catch (ResourceAccessException e) {
			log.error("Google Maps Text Search API 요청 실패: {}", e.getMessage(), e);
			if (e.getCause() instanceof SocketTimeoutException) {
				throw new GlobalException(GoogleMapsErrorCode.API_TIMEOUT);
			}
			throw new GlobalException(GoogleMapsErrorCode.API_CALL_FAILED);
		}
	}

	public GooglePlaceNearbySearchResponse searchNearbyPlaces(final double latitude, final double longitude) {
		try {
			log.info("Google Maps NearbySearch API 호출: latitude={}, longitude={}", latitude, longitude);

			final GooglePlaceNearbySearchResponse response = googleMapsPlaceRestClient.post()
				.uri(uriBuilder -> {
					return uriBuilder
						.path("/places:searchNearby")
						.queryParam("languageCode", "ko")
						.build();
				})
				.header(GOOGLE_MAPS_KEY_HEADER, apiKey)
				.header(FIELD_MASK_HEADER, NEARBY_SEARCH_FIELD_MASK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(PlaceNearbySearchRequest.of(latitude, longitude))
				.retrieve()
				.onStatus(HttpStatusCode::isError, (req, res) -> {
					log.error("Google Maps NearbySearch API 응답 오류 (status={})", res.getStatusCode());
					throw new GlobalException(GoogleMapsErrorCode.API_CALL_FAILED);
				})
				.body(GooglePlaceNearbySearchResponse.class);

			if (response == null) {
				throw new GlobalException(GoogleMapsErrorCode.API_CALL_FAILED);
			}

			return response;

		} catch (ResourceAccessException e) {
			log.error("Google Maps NearbySearch API 요청 실패: {}", e.getMessage(), e);
			if (e.getCause() instanceof SocketTimeoutException) {
				throw new GlobalException(GoogleMapsErrorCode.API_TIMEOUT);
			}
			throw new GlobalException(GoogleMapsErrorCode.API_CALL_FAILED);
		}
	}

	private void validateRequest(final PlaceDetailsRequest request) {
		if (request == null || !StringUtils.hasText(request.googlePlaceId())) {
			throw new GlobalException(GoogleMapsErrorCode.INVALID_PLACE_ID);
		}
	}

	private void validateResponse(final GooglePlaceDetailsResponse response) {
		if (response == null) {
			throw new GlobalException(GoogleMapsErrorCode.API_CALL_FAILED);
		}
	}
}
