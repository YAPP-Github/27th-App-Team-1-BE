package com.yapp.ndgl.clients.google.places.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Google Maps Place Details API 응답 모델.
 */
public record PlaceDetailsResponse(
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
	DisplayName displayName,
	List<Photo> photos,
	PostalAddress postalAddress
) {

	@JsonCreator
	public PlaceDetailsResponse(
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
		@JsonProperty("displayName") final DisplayName displayName,
		@JsonProperty("photos") final List<Photo> photos,
		@JsonProperty("postalAddress") final PostalAddress postalAddress
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
		this.displayName = displayName;
		this.photos = photos;
		this.postalAddress = postalAddress;
	}

	public record Location(Double latitude, Double longitude) {

		@JsonCreator
		public Location(
			@JsonProperty("latitude") final Double latitude,
			@JsonProperty("longitude") final Double longitude
		) {
			this.latitude = latitude;
			this.longitude = longitude;
		}
	}

	public record DisplayName(String text, String languageCode) {

		@JsonCreator
		public DisplayName(
			@JsonProperty("text") final String text,
			@JsonProperty("languageCode") final String languageCode
		) {
			this.text = text;
			this.languageCode = languageCode;
		}
	}

	public record RegularOpeningHours(Boolean openNow, List<Period> periods, List<String> weekdayDescriptions) {

		@JsonCreator
		public RegularOpeningHours(
			@JsonProperty("openNow") final Boolean openNow,
			@JsonProperty("periods") final List<Period> periods,
			@JsonProperty("weekdayDescriptions") final List<String> weekdayDescriptions
		) {
			this.openNow = openNow;
			this.periods = periods;
			this.weekdayDescriptions = weekdayDescriptions;
		}
	}

	public record Period(DayTime open, DayTime close) {

		@JsonCreator
		public Period(
			@JsonProperty("open") final DayTime open,
			@JsonProperty("close") final DayTime close
		) {
			this.open = open;
			this.close = close;
		}
	}

	public record DayTime(Integer day, Integer hour, Integer minute) {

		@JsonCreator
		public DayTime(
			@JsonProperty("day") final Integer day,
			@JsonProperty("hour") final Integer hour,
			@JsonProperty("minute") final Integer minute
		) {
			this.day = day;
			this.hour = hour;
			this.minute = minute;
		}
	}

	public record Photo(
		String name,
		Integer widthPx,
		Integer heightPx,
		List<AuthorAttribution> authorAttributions,
		String flagContentUri,
		String googleMapsUri) {

		@JsonCreator
		public Photo(
			@JsonProperty("name") final String name,
			@JsonProperty("widthPx") final Integer widthPx,
			@JsonProperty("heightPx") final Integer heightPx,
			@JsonProperty("authorAttributions") final List<AuthorAttribution> authorAttributions,
			@JsonProperty("flagContentUri") final String flagContentUri,
			@JsonProperty("googleMapsUri") final String googleMapsUri
		) {
			this.name = name;
			this.widthPx = widthPx;
			this.heightPx = heightPx;
			this.authorAttributions = authorAttributions;
			this.flagContentUri = flagContentUri;
			this.googleMapsUri = googleMapsUri;
		}
	}

	public record AuthorAttribution(
		String displayName,
		String uri,
		String photoUri) {

		@JsonCreator
		public AuthorAttribution(
			@JsonProperty("displayName") final String displayName,
			@JsonProperty("uri") final String uri,
			@JsonProperty("photoUri") final String photoUri
		) {
			this.displayName = displayName;
			this.uri = uri;
			this.photoUri = photoUri;
		}
	}

	public record PostalAddress(
		String regionCode,
		String languageCode,
		String postalCode,
		String administrativeArea,
		List<String> addressLines
	) {

		@JsonCreator
		public PostalAddress(
			@JsonProperty("regionCode") final String regionCode,
			@JsonProperty("languageCode") final String languageCode,
			@JsonProperty("postalCode") final String postalCode,
			@JsonProperty("administrativeArea") final String administrativeArea,
			@JsonProperty("addressLines") final List<String> addressLines
		) {
			this.regionCode = regionCode;
			this.languageCode = languageCode;
			this.postalCode = postalCode;
			this.administrativeArea = administrativeArea;
			this.addressLines = addressLines;
		}
	}
}
