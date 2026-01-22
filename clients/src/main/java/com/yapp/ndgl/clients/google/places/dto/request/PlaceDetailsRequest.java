package com.yapp.ndgl.clients.google.places.dto.request;

import lombok.Builder;

/**
 * Google Maps Place Details API 요청 파라미터를 구성한다.
 */
@Builder
public record PlaceDetailsRequest(
	String googlePlaceId, String language) {

	public static PlaceDetailsRequest of(final String googlePlaceId, final String language) {
		return PlaceDetailsRequest.builder()
			.googlePlaceId(googlePlaceId)
			.language(language)
			.build();
	}

}
