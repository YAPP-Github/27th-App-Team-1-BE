package com.yapp.ndgl.clients.google.places;

import java.net.SocketTimeoutException;
import java.net.URI;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import com.yapp.ndgl.clients.google.places.dto.PlaceDetailsRequest;
import com.yapp.ndgl.clients.google.places.dto.PlaceDetailsResponse;
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
public class GoogleMapsPlaceClient {

	private static final String FIELD_MASK_HEADER = "X-Goog-FieldMask";
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
		"userRatingCount"
	);

	private final RestClient googleMapsRestClient;

	/**
	 * Place Details API를 호출하고 응답 상태를 검증한다.
	 *
	 * @param request Place Details API 요청 파라미터
	 * @return 검증된 Place Details 응답
	 */
	public PlaceDetailsResponse readPlaceDetails(final PlaceDetailsRequest request) {
		try {
			validateRequest(request);

			final String uri = "/places/" + request.placeId();

			RestClient.ResponseSpec spec = googleMapsRestClient.get()
				.uri(uriBuilder -> {
					final URI requestURI = uriBuilder
						.path(uri)
						.queryParam("languageCode", request.language())
						.build();

					log.info("API 요청 URI = {}", requestURI);
					return requestURI;
				})
				.header(FIELD_MASK_HEADER, DEFAULT_FIELD_MASK)
				.retrieve()
				.onStatus(HttpStatusCode::isError, (req, res) -> {
					log.error("Google Maps Place API 응답 오류 - 장소 세부 정보 조회시 오류 발생 (status={})", res.getStatusCode());
					throw new GlobalException(GoogleMapsErrorCode.API_CALL_FAILED);
				});

			final PlaceDetailsResponse response = spec.body(PlaceDetailsResponse.class);
			log.debug("Google Maps Place Details API 호출 성공: placeId={}", request.placeId());
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

	private void validateRequest(final PlaceDetailsRequest request) {
		if (request == null || !StringUtils.hasText(request.placeId())) {
			throw new GlobalException(GoogleMapsErrorCode.INVALID_PLACE_ID);
		}
	}

	private void validateResponse(final PlaceDetailsResponse response) {
		if (response == null) {
			throw new GlobalException(GoogleMapsErrorCode.API_CALL_FAILED);
		}
	}
}
