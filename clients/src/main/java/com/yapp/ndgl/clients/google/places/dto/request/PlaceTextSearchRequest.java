package com.yapp.ndgl.clients.google.places.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlaceTextSearchRequest(
	@JsonProperty("textQuery") String textQuery,
	@JsonProperty("locationBias") LocationBias locationBias,
	@JsonProperty("maxResultCount") int maxResultCount) {

	public static PlaceTextSearchRequest of(final String textQuery) {
		return PlaceTextSearchRequest.builder()
			.textQuery(textQuery)
			.maxResultCount(1)
			.build();
	}

	public static PlaceTextSearchRequest of(final String textQuery, final double latitude, final double longitude) {
		return PlaceTextSearchRequest.builder()
			.textQuery(textQuery)
			.locationBias(LocationBias.ofCircle(latitude, longitude))
			.maxResultCount(1)
			.build();
	}

	public record LocationBias(@JsonProperty("circle") Circle circle) {
		private static final double DEFAULT_RADIUS = 5000.0;

		public static LocationBias ofCircle(final double latitude, final double longitude) {
			return new LocationBias(new Circle(new LatLng(latitude, longitude), DEFAULT_RADIUS));
		}
	}

	public record Circle(
		@JsonProperty("center") LatLng center,
		@JsonProperty("radius") double radius) {
	}

	public record LatLng(
		@JsonProperty("latitude") double latitude,
		@JsonProperty("longitude") double longitude) {
	}
}
