package com.yapp.ndgl.clients.google.places;

import java.net.SocketTimeoutException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import com.yapp.ndgl.clients.google.places.dto.request.PlacePhotoRequest;
import com.yapp.ndgl.clients.google.places.dto.response.PlacePhotoResponse;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.GoogleMapsErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring RestClient로 Google Maps Place Photo API를 호출하는 클라이언트.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleMapsPlacePhotoClient {

	@Value("${google.maps.api-key}")
	private String apiKey;
	private final RestClient googleMapsPlaceRestClient;

	/**
	 * Place Photo API를 호출하여 사진 URI를 조회한다.
	 *
	 * @param request Place Photo API 요청 파라미터
	 * @return 사진 URI가 포함된 응답
	 */
	public PlacePhotoResponse getPhotoUri(final PlacePhotoRequest request) {
		try {
			validateRequest(request);

			RestClient.ResponseSpec spec = googleMapsPlaceRestClient.get()
				.uri(uriBuilder -> {
					URI requestURI = uriBuilder
						.path("/" + request.photoName())
						.queryParam("key", apiKey)
						.queryParam("maxHeightPx", request.maxHeightPx())
						.queryParam("maxWidthPx", request.maxWidthPx())
						.queryParam("skipHttpRedirect", true)
						.build();

					log.info("Place Photo API 요청 URI = {}", requestURI);
					return requestURI;
				})
				.retrieve()
				.onStatus(HttpStatusCode::isError, (req, res) -> {
					log.error("Google Maps Place Photo API 응답 오류 (status={})", res.getStatusCode());
					throw new GlobalException(GoogleMapsErrorCode.API_CALL_FAILED);
				});

			PlacePhotoResponse response = spec.body(PlacePhotoResponse.class);
			log.info("Google Maps Place Photo API 호출 성공: photoName={}", request.photoName());
			validateResponse(response);

			return response;

		} catch (ResourceAccessException e) {
			log.error("Google Maps Place Photo API 요청 실패: {}", e.getMessage(), e);
			if (e.getCause() instanceof SocketTimeoutException) {
				throw new GlobalException(GoogleMapsErrorCode.API_TIMEOUT);
			}
			throw new GlobalException(GoogleMapsErrorCode.API_CALL_FAILED);
		}
	}

	private void validateRequest(final PlacePhotoRequest request) {
		if (request == null || !StringUtils.hasText(request.photoName())) {
			throw new GlobalException(GoogleMapsErrorCode.INVALID_PHOTO_NAME);
		}
	}

	private void validateResponse(final PlacePhotoResponse response) {
		if (response == null || !StringUtils.hasText(response.uri())) {
			throw new GlobalException(GoogleMapsErrorCode.API_CALL_FAILED);
		}
	}
}
