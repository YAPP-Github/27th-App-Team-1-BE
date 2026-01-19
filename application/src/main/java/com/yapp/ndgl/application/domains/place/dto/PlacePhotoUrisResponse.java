package com.yapp.ndgl.application.domains.place.dto;

import java.util.List;

/**
 * Photo URI 목록 응답 DTO.
 */
public record PlacePhotoUrisResponse(
	List<PhotoUri> photoUris
) {

	public record PhotoUri(
		String name,
		Integer widthPx,
		Integer heightPx,
		String photoUri
	) {

		public static PhotoUri of(
			final String name,
			final Integer widthPx,
			final Integer heightPx,
			final String photoUri
		) {
			return new PhotoUri(name, widthPx, heightPx, photoUri);
		}
	}

	public static PlacePhotoUrisResponse from(final List<PhotoUri> photoUris) {
		return new PlacePhotoUrisResponse(photoUris);
	}

	public static PlacePhotoUrisResponse empty() {
		return new PlacePhotoUrisResponse(List.of());
	}
}
