package com.yapp.ndgl.clients.google.places.dto.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlaceNearbySearchRequest(
	@JsonProperty("includedTypes") List<String> includedTypes,
	@JsonProperty("maxResultCount") int maxResultCount,
	@JsonProperty("locationRestriction") LocationRestriction locationRestriction
) {

	private static final int DEFAULT_MAX_RESULT_COUNT = 3;
	private static final double DEFAULT_RADIUS = 1000;

	public static PlaceNearbySearchRequest of(final double latitude, final double longitude, final String primaryType) {
		List<String> includedTypes = (primaryType != null && !primaryType.isBlank())
			? List.of(primaryType)
			: null;
		return new PlaceNearbySearchRequest(
			includedTypes,
			DEFAULT_MAX_RESULT_COUNT,
			LocationRestriction.ofCircle(latitude, longitude)
		);
	}

	public record LocationRestriction(@JsonProperty("circle") Circle circle) {

		public static LocationRestriction ofCircle(final double latitude, final double longitude) {
			return new LocationRestriction(new Circle(new LatLng(latitude, longitude), DEFAULT_RADIUS));
		}
	}

	public record Circle(
		@JsonProperty("center") LatLng center,
		@JsonProperty("radius") double radius
	) {
	}

	public record LatLng(
		@JsonProperty("latitude") double latitude,
		@JsonProperty("longitude") double longitude
	) {
	}
}
