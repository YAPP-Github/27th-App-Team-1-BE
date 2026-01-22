package com.yapp.ndgl.application.domains.place.controller.response;

import java.util.List;

import com.yapp.ndgl.domain.place.PlacePhoto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Photo URI 목록 응답 DTO.
 */

public record PlacePhotoResponse(
	@Schema(description = "사진 URI 목록", requiredMode = Schema.RequiredMode.REQUIRED)
	List<PhotoUri> photos
) {

	public static PlacePhotoResponse empty() {
		return new PlacePhotoResponse(List.of());
	}

	public static PlacePhotoResponse toResponse(final List<PlacePhoto> photos) {

		if (photos == null || photos.isEmpty()) {
			return empty();
		}

		List<PlacePhotoResponse.PhotoUri> photoUris = photos.stream()
			.map(photo -> PlacePhotoResponse.PhotoUri.of(
				photo.getPhotoUri(),
				photo.getWidthPx(),
				photo.getHeightPx()
			))
			.toList();

		return new PlacePhotoResponse(photoUris);
	}

	public record PhotoUri(
		@Schema(description = "사진 URI", example = "https://lh3.googleusercontent.com/places/ANXAkqGbrXxA6_1tCvdNIB0BxCAB1ovsQH2KHOY5sBZEDZ8MI86hg5WIjXA0Ts_l3lKhJre-RpTvHVSMKusLWnwsDCW3HbqOAx6l3D0=s4800-w4800-h3200", requiredMode = Schema.RequiredMode.REQUIRED)
		String photoUri,
		@Schema(description = "사진 너비(px)", example = "4032", requiredMode = Schema.RequiredMode.REQUIRED)
		Integer widthPx,
		@Schema(description = "사진 높이(px)", example = "3024", requiredMode = Schema.RequiredMode.REQUIRED)
		Integer heightPx
	) {
		public static PhotoUri of(
			final String photoUri,
			final Integer widthPx,
			final Integer heightPx
		) {
			return new PhotoUri(photoUri, widthPx, heightPx);
		}
	}
}
