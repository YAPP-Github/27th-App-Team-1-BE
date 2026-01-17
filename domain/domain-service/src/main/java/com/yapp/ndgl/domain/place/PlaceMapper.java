package com.yapp.ndgl.domain.place;

import com.yapp.ndgl.domain.place.entity.PlaceEntity;

public class PlaceMapper {

	public static PlaceEntity toEntity(final Place place) {
		return PlaceEntity.builder()
			.placeId(place.getPlaceId())
			.formattedAddress(place.getFormattedAddress())
			.latitude(place.getLatitude())
			.longitude(place.getLongitude())
			.rating(place.getRating())
			.nationalPhoneNumber(place.getNationalPhoneNumber())
			.internationalPhoneNumber(place.getInternationalPhoneNumber())
			.websiteUri(place.getWebsiteUri())
			.googleMapsUri(place.getGoogleMapsUri())
			.userRatingCount(place.getUserRatingCount())
			.displayNameJson(place.getDisplayNameJson())
			.regularOpeningHoursJson(place.getRegularOpeningHoursJson())
			.photosJson(place.getPhotosJson())
			.postalAddressJson(place.getPostalAddressJson())
			.build();
	}

	public static Place toDomain(final PlaceEntity entity) {
		return Place.builder()
			.id(entity.getId())
			.placeId(entity.getPlaceId())
			.formattedAddress(entity.getFormattedAddress())
			.latitude(entity.getLatitude())
			.longitude(entity.getLongitude())
			.rating(entity.getRating())
			.nationalPhoneNumber(entity.getNationalPhoneNumber())
			.internationalPhoneNumber(entity.getInternationalPhoneNumber())
			.websiteUri(entity.getWebsiteUri())
			.googleMapsUri(entity.getGoogleMapsUri())
			.userRatingCount(entity.getUserRatingCount())
			.displayNameJson(entity.getDisplayNameJson())
			.regularOpeningHoursJson(entity.getRegularOpeningHoursJson())
			.photosJson(entity.getPhotosJson())
			.postalAddressJson(entity.getPostalAddressJson())
			.createdAt(entity.getCreatedAt())
			.updatedAt(entity.getUpdatedAt())
			.build();
	}
}
