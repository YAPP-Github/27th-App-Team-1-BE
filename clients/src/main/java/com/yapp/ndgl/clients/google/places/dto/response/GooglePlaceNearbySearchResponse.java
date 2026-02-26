package com.yapp.ndgl.clients.google.places.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GooglePlaceNearbySearchResponse(
	@JsonProperty("places") List<PlaceResult> places
) {

	public record PlaceResult(
		@JsonProperty("id") String id,
		@JsonProperty("displayName") DisplayName displayName,
		@JsonProperty("location") Location location,
		@JsonProperty("photos") List<PhotoMeta> photos,
		@JsonProperty("rating") Double rating,
		@JsonProperty("userRatingCount") Integer userRatingCount,
		@JsonProperty("formattedAddress") String formattedAddress,
		@JsonProperty("nationalPhoneNumber") String nationalPhoneNumber,
		@JsonProperty("internationalPhoneNumber") String internationalPhoneNumber,
		@JsonProperty("websiteUri") String websiteUri,
		@JsonProperty("googleMapsUri") String googleMapsUri,
		@JsonProperty("regularOpeningHours") RegularOpeningHours regularOpeningHours,
		@JsonProperty("primaryType") String primaryType,
		@JsonProperty("types") List<String> types,
		@JsonProperty("priceRange") PriceRange priceRange
	) {
	}

	public record DisplayName(
		@JsonProperty("text") String text
	) {
	}

	public record Location(
		@JsonProperty("latitude") Double latitude,
		@JsonProperty("longitude") Double longitude
	) {
	}

	public record PhotoMeta(
		@JsonProperty("name") String name,
		@JsonProperty("widthPx") Integer widthPx,
		@JsonProperty("heightPx") Integer heightPx
	) {
	}

	public record RegularOpeningHours(
		@JsonProperty("weekdayDescriptions") List<String> weekdayDescriptions
	) {
	}

	public record PriceRange(
		@JsonProperty("startPrice") Money startPrice,
		@JsonProperty("endPrice") Money endPrice
	) {
	}

	public record Money(
		@JsonProperty("currencyCode") String currencyCode,
		@JsonProperty("units") String units
	) {
	}
}
