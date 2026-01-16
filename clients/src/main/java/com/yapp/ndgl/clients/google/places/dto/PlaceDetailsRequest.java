package com.yapp.ndgl.clients.google.places.dto;

import lombok.Builder;

/**
 * Google Maps Place Details API 요청 파라미터를 구성한다.
 */
@Builder
public record PlaceDetailsRequest(
	String placeId, String language) {

	public static PlaceDetailsRequest of(final String placeId, final String language) {
		return PlaceDetailsRequest.builder()
			.placeId(placeId)
			.language(language)
			.build();
	}

	// private final String placeId = "ChIJSc8jdZORQTURu6BMwxrKbGg";
	//
	// @Builder.Default
	// private final String language = "ko";


}
