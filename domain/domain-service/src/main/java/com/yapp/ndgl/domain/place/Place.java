package com.yapp.ndgl.domain.place;

import java.time.LocalDateTime;
import java.util.List;

import com.yapp.ndgl.common.type.PhotoMeta;
import com.yapp.ndgl.common.type.PlaceCategory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Place {

	private final Long id;
	private final String googlePlaceId;
	private final String formattedAddress;
	private final Double latitude;
	private final Double longitude;
	private final Double rating;
	private final String nationalPhoneNumber;
	private final String internationalPhoneNumber;
	private final String websiteUri;
	private final String googleMapsUri;
	private final Integer userRatingCount;
	private final String name;
	private final String thumbnail;
	private final List<String> regularOpeningHours;
	private final List<PhotoMeta> photos;
	private final String priceCurrencyCode;
	private final String priceStartUnits;
	private final String priceEndUnits;
	private final PlaceCategory category;
	private final String primaryType;
	private final List<String> nearbyPlaces;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	public static Place create(
		final String googlePlaceId,
		final String formattedAddress,
		final Double latitude,
		final Double longitude,
		final Double rating,
		final String nationalPhoneNumber,
		final String internationalPhoneNumber,
		final String websiteUri,
		final String googleMapsUri,
		final Integer userRatingCount,
		final String name,
		final String thumbnail,
		final List<String> regularOpeningHours,
		final List<PhotoMeta> photos,
		final String priceCurrencyCode,
		final String priceStartUnits,
		final String priceEndUnits,
		final PlaceCategory category,
		final String primaryType
	) {
		return Place.builder()
			.googlePlaceId(googlePlaceId)
			.formattedAddress(formattedAddress)
			.latitude(latitude)
			.longitude(longitude)
			.rating(rating)
			.nationalPhoneNumber(nationalPhoneNumber)
			.internationalPhoneNumber(internationalPhoneNumber)
			.websiteUri(websiteUri)
			.googleMapsUri(googleMapsUri)
			.userRatingCount(userRatingCount)
			.name(name)
			.thumbnail(thumbnail)
			.regularOpeningHours(regularOpeningHours)
			.photos(photos)
			.priceCurrencyCode(priceCurrencyCode)
			.priceStartUnits(priceStartUnits)
			.priceEndUnits(priceEndUnits)
			.category(category)
			.primaryType(primaryType)
			.build();
	}
}
