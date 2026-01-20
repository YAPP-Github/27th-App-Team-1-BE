package com.yapp.ndgl.application.domains.place.dto;

import java.util.List;
import java.util.Objects;

import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceDetailsResponse;

/**
 * 장소 세부 정보 최종 응답 DTO.
 * GooglePlaceDetailsResponse와 PlacePhotoUrisResponse를 합친 결과.
 */
public record PlaceDetailResponse(
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
	List<Photo> photos
	) {

	public record Location(Double latitude, Double longitude) {

		public static Location from(final GooglePlaceDetailsResponse.Location location) {
			if (location == null) {
				return null;
			}
			return new Location(location.latitude(), location.longitude());
		}
	}

	public record Photo(
		String name,
		Integer widthPx,
		Integer heightPx,
		String photoUri
	) {

		public static Photo of(
			final GooglePlaceDetailsResponse.PhotoMeta googlePhoto,
			final String photoUri
		) {
			if (googlePhoto == null) {
				return null;
			}
			return new Photo(
				googlePhoto.name(),
				googlePhoto.widthPx(),
				googlePhoto.heightPx(),
				photoUri
			);
		}
	}

	public static PlaceDetailResponse of(
		final GooglePlaceDetailsResponse googleResponse,
		final PlacePhotoUrisResponse photoResponse
	) {
		List<Photo> photos = null;
		if (googleResponse.photos() != null) {
			photos = googleResponse.photos().stream()
				.map(googlePhoto -> {
					String photoUri = findPhotoUri(googlePhoto.name(), photoResponse);
					return Photo.of(googlePhoto, photoUri);
				})
				.toList();
		}

		return new PlaceDetailResponse(
			googleResponse.id(),
			googleResponse.displayName().text(),
			googleResponse.nationalPhoneNumber(),
			googleResponse.internationalPhoneNumber(),
			googleResponse.formattedAddress(),
			Location.from(googleResponse.location()),
			googleResponse.userRatingCount(),
			googleResponse.rating(),
			googleResponse.regularOpeningHours().weekdayDescriptions(),
			googleResponse.googleMapsUri(),
			googleResponse.websiteUri(),
			photos
			);
	}

	private static String findPhotoUri(final String photoName, final PlacePhotoUrisResponse photoResponse) {
		if (photoResponse == null || photoResponse.photoUris() == null) {
			return null;
		}

		return photoResponse.photoUris().stream()
			.filter(p -> Objects.equals(p.name(), photoName))
			.findFirst()
			.map(PlacePhotoUrisResponse.PhotoUri::photoUri)
			.orElse(null);

	}
}
