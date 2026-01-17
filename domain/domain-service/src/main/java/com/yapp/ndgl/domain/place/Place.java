package com.yapp.ndgl.domain.place;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Place {

	private final Long id;
	private final String placeId;
	private final String formattedAddress;
	private final Double latitude;
	private final Double longitude;
	private final Double rating;
	private final String nationalPhoneNumber;
	private final String internationalPhoneNumber;
	private final String websiteUri;
	private final String googleMapsUri;
	private final Integer userRatingCount;
	private final String displayNameJson;
	private final String regularOpeningHoursJson;
	private final String photosJson;
	private final String postalAddressJson;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	public static Place create(
		final String placeId,
		final String formattedAddress,
		final Double latitude,
		final Double longitude,
		final Double rating,
		final String nationalPhoneNumber,
		final String internationalPhoneNumber,
		final String websiteUri,
		final String googleMapsUri,
		final Integer userRatingCount,
		final String displayNameJson,
		final String regularOpeningHoursJson,
		final String photosJson,
		final String postalAddressJson
	) {
		LocalDateTime now = LocalDateTime.now();

		return Place.builder()
			.placeId(placeId)
			.formattedAddress(formattedAddress)
			.latitude(latitude)
			.longitude(longitude)
			.rating(rating)
			.nationalPhoneNumber(nationalPhoneNumber)
			.internationalPhoneNumber(internationalPhoneNumber)
			.websiteUri(websiteUri)
			.googleMapsUri(googleMapsUri)
			.userRatingCount(userRatingCount)
			.displayNameJson(displayNameJson)
			.regularOpeningHoursJson(regularOpeningHoursJson)
			.photosJson(photosJson)
			.postalAddressJson(postalAddressJson)
			.createdAt(now)
			.updatedAt(now)
			.build();
	}
}
