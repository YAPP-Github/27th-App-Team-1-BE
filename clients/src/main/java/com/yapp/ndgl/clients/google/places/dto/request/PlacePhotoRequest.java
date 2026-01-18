package com.yapp.ndgl.clients.google.places.dto.request;

import lombok.Builder;

/**
 * Google Maps Place Photo API 요청 파라미터.
 */
@Builder
public record PlacePhotoRequest(
	String photoName,
	Integer maxHeightPx,
	Integer maxWidthPx
) {

	public static PlacePhotoRequest of(
		final String photoName,
		final Integer maxHeightPx,
		final Integer maxWidthPx
	) {
		return PlacePhotoRequest.builder()
			.photoName(photoName)
			.maxHeightPx(maxHeightPx)
			.maxWidthPx(maxWidthPx)
			.build();
	}
}
