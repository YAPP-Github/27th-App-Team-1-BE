package com.yapp.ndgl.domain.place.mapper;

import com.yapp.ndgl.common.type.PlaceCategory;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.entity.PlaceEntity;

public class PlaceMapper {

	public static PlaceEntity toEntity(final Place place) {
		return PlaceEntity.builder()
			.googlePlaceId(place.getGooglePlaceId())
			.formattedAddress(place.getFormattedAddress())
			.latitude(place.getLatitude())
			.longitude(place.getLongitude())
			.rating(place.getRating())
			.nationalPhoneNumber(place.getNationalPhoneNumber())
			.internationalPhoneNumber(place.getInternationalPhoneNumber())
			.websiteUri(place.getWebsiteUri())
			.googleMapsUri(place.getGoogleMapsUri())
			.userRatingCount(place.getUserRatingCount())
			.name(place.getName())
			.thumbnail(place.getThumbnail())
			.regularOpeningHours(place.getRegularOpeningHours())
			.photosJson(place.getPhotosJson())
			.priceCurrencyCode(place.getPriceCurrencyCode())
			.priceStartUnits(place.getPriceStartUnits())
			.priceEndUnits(place.getPriceEndUnits())
			.category(place.getCategory() != null ? place.getCategory() : PlaceCategory.ATTRACTION)
			.nearbyPlacesJson(place.getNearbyPlacesJson())
			.build();
	}

	public static Place toDomain(final PlaceEntity entity) {
		return Place.builder()
			.id(entity.getId())
			.googlePlaceId(entity.getGooglePlaceId())
			.formattedAddress(entity.getFormattedAddress())
			.latitude(entity.getLatitude())
			.longitude(entity.getLongitude())
			.rating(entity.getRating())
			.nationalPhoneNumber(entity.getNationalPhoneNumber())
			.internationalPhoneNumber(entity.getInternationalPhoneNumber())
			.websiteUri(entity.getWebsiteUri())
			.googleMapsUri(entity.getGoogleMapsUri())
			.userRatingCount(entity.getUserRatingCount())
			.name(entity.getName())
			.thumbnail(entity.getThumbnail())
			.regularOpeningHours(entity.getRegularOpeningHours())
			.photosJson(entity.getPhotosJson())
			.priceCurrencyCode(entity.getPriceCurrencyCode())
			.priceStartUnits(entity.getPriceStartUnits())
			.priceEndUnits(entity.getPriceEndUnits())
			.category(entity.getCategory() != null ? entity.getCategory() : PlaceCategory.ATTRACTION)
			.nearbyPlacesJson(entity.getNearbyPlacesJson())
			.createdAt(entity.getCreatedAt())
			.updatedAt(entity.getUpdatedAt())
			.build();
	}
}
