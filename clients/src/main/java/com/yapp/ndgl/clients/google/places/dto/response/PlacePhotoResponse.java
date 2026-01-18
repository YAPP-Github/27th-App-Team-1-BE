package com.yapp.ndgl.clients.google.places.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * Google Maps Place Photo API 응답 모델.
 */
public record PlacePhotoResponse(
	@JsonAlias("photoUri") String uri
) {
}
