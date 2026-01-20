package com.yapp.ndgl.application.domains.place.dto;

import java.util.List;

import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceDetailsResponse;

/**
 * 장소 세부 정보 Service 레이어 응답 DTO.
 */
public record PlaceInfoResponse(
	String id,
	String name,
	String nationalPhoneNumber,
	String internationalPhoneNumber,
	String formattedAddress,
	Location location,
	Integer userRatingCount,
	Double rating,
	List<String> regularOpeningHours,
	String googleMapsUri,
	String websiteUri,
	List<PhotoMeta> photos
) {

	public record Location(Double latitude, Double longitude) {
		public static Location from(final GooglePlaceDetailsResponse.Location location) {
			if (location == null) {
				return null;
			}
			return new Location(location.latitude(), location.longitude());
		}
	}

	public record PhotoMeta(
		String name,
		Integer widthPx,
		Integer heightPx,
		String flagContentUri,
		String googleMapsUri
	) {
		public static PhotoMeta from(final GooglePlaceDetailsResponse.PhotoMeta photoMeta) {
			if (photoMeta == null) {
				return null;
			}
			return new PhotoMeta(
				photoMeta.name(),
				photoMeta.widthPx(),
				photoMeta.heightPx(),
				photoMeta.flagContentUri(),
				photoMeta.googleMapsUri()
			);
		}
	}

	public static PlaceInfoResponse from(final GooglePlaceDetailsResponse response) {
		if (response == null) {
			return null;
		}

		List<PhotoMeta> photos = null;
		if (response.photos() != null) {
			photos = response.photos().stream()
				.map(PhotoMeta::from)
				.toList();
		}

		String name = response.name() != null ? response.name().text() : null;

		return new PlaceInfoResponse(
			response.id(),
			name,
			response.nationalPhoneNumber(),
			response.internationalPhoneNumber(),
			response.formattedAddress(),
			Location.from(response.location()),
			response.userRatingCount(),
			response.rating(),
			response.regularOpeningHours().regularOpeningHours(),
			response.googleMapsUri(),
			response.websiteUri(),
			photos
		);
	}
}
