package com.yapp.ndgl.clients.google.places.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Google Maps Place Details API 응답 모델.
 */
public record GooglePlaceDetailsResponse(
	String id,
	String nationalPhoneNumber,
	String internationalPhoneNumber,
	String formattedAddress,
	Location location,
	Double rating,
	String googleMapsUri,
	String websiteUri,
	RegularOpeningHours regularOpeningHours,
	Integer userRatingCount,
	DisplayName name,
	List<PhotoMeta> photos
) {

	public GooglePlaceDetailsResponse(
		@JsonProperty("id") final String id,
		@JsonProperty("nationalPhoneNumber") final String nationalPhoneNumber,
		@JsonProperty("internationalPhoneNumber") final String internationalPhoneNumber,
		@JsonProperty("formattedAddress") final String formattedAddress,
		@JsonProperty("location") final Location location,
		@JsonProperty("rating") final Double rating,
		@JsonProperty("googleMapsUri") final String googleMapsUri,
		@JsonProperty("websiteUri") final String websiteUri,
		@JsonProperty("regularOpeningHours") final RegularOpeningHours regularOpeningHours,
		@JsonProperty("userRatingCount") final Integer userRatingCount,
		@JsonProperty("displayName") final DisplayName name,
		@JsonProperty("photos") final List<PhotoMeta> photos
	) {
		this.id = id;
		this.nationalPhoneNumber = nationalPhoneNumber;
		this.internationalPhoneNumber = internationalPhoneNumber;
		this.formattedAddress = formattedAddress;
		this.location = location;
		this.rating = rating;
		this.googleMapsUri = googleMapsUri;
		this.websiteUri = websiteUri;
		this.regularOpeningHours = regularOpeningHours;
		this.userRatingCount = userRatingCount;
		this.name = name;
		this.photos = photos;
	}

	public record Location(Double latitude, Double longitude) {

		public Location(
			@JsonProperty("latitude") final Double latitude,
			@JsonProperty("longitude") final Double longitude
		) {
			this.latitude = latitude;
			this.longitude = longitude;
		}
	}

	public record DisplayName(String text) {

		public DisplayName(
			@JsonProperty("text") final String text
		) {
			this.text = text;
		}
	}

	public record RegularOpeningHours(List<String> regularOpeningHours) {

		public RegularOpeningHours(
			@JsonProperty("weekdayDescriptions") final List<String> regularOpeningHours
		) {
			this.regularOpeningHours = regularOpeningHours;
		}
	}

	public record PhotoMeta(
		String name,
		Integer widthPx,
		Integer heightPx,
		String flagContentUri,
		String googleMapsUri) {

		public PhotoMeta(
			@JsonProperty("name") final String name,
			@JsonProperty("widthPx") final Integer widthPx,
			@JsonProperty("heightPx") final Integer heightPx,
			@JsonProperty("flagContentUri") final String flagContentUri,
			@JsonProperty("googleMapsUri") final String googleMapsUri
		) {
			this.name = name;
			this.widthPx = widthPx;
			this.heightPx = heightPx;
			this.flagContentUri = flagContentUri;
			this.googleMapsUri = googleMapsUri;
		}
	}
}
