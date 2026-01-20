package com.yapp.ndgl.domain.place;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlacePhoto {

	private final Long id;
	private final String placeId;
	private final String photoName;
	private final String photoUri;
	private final Integer widthPx;
	private final Integer heightPx;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	public static PlacePhoto create(
		final String placeId,
		final String photoName,
		final String photoUri,
		final Integer widthPx,
		final Integer heightPx
	) {
		return PlacePhoto.builder()
			.placeId(placeId)
			.photoName(photoName)
			.photoUri(photoUri)
			.widthPx(widthPx)
			.heightPx(heightPx)
			.build();
	}
}
