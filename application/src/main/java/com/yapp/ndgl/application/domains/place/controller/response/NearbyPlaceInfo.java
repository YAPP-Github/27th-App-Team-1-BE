package com.yapp.ndgl.application.domains.place.controller.response;

import com.yapp.ndgl.common.type.PlaceCategory;
import com.yapp.ndgl.domain.place.Place;

import io.swagger.v3.oas.annotations.media.Schema;

public record NearbyPlaceInfo(
	@Schema(description = "Google Places 장소 ID")
	String googlePlaceId,
	@Schema(description = "장소 이름")
	String name,
	@Schema(description = "장소 썸네일", nullable = true)
	String thumbnail,
	@Schema(description = "장소 카테고리")
	PlaceCategory category,
	@Schema(description = "평점", nullable = true)
	Double rating,
	@Schema(description = "위도")
	Double latitude,
	@Schema(description = "경도")
	Double longitude
) {

	public static NearbyPlaceInfo from(final Place place) {
		return new NearbyPlaceInfo(
			place.getGooglePlaceId(),
			place.getName(),
			place.getThumbnail(),
			place.getCategory(),
			place.getRating(),
			place.getLatitude(),
			place.getLongitude()
		);
	}
}
