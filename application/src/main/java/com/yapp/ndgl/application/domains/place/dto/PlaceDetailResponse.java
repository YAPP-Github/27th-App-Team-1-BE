package com.yapp.ndgl.application.domains.place.dto;

import java.util.List;
import java.util.Objects;

/**
 * 장소 세부 정보 최종 응답 DTO.
 * PlaceDetailInfoResponse와 PlacePhotoUrisResponse를 합친 결과.
 */
public record PlaceDetailResponse(
	String id,
	String name,
	String thumbnail,
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

		public static Location from(final PlaceInfoResponse.Location location) {
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
			final PlaceInfoResponse.PhotoMeta googlePhoto,
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
		final PlaceInfoResponse placeInfoResponse,
		final PlacePhotoUrisResponse photoResponse
	) {
		List<Photo> photos = null;
		if (placeInfoResponse.photos() != null) {
			photos = placeInfoResponse.photos().stream()
				.map(googlePhoto -> {
					String photoUri = findPhotoUri(googlePhoto.name(), photoResponse);
					return Photo.of(googlePhoto, photoUri);
				})
				.toList();
		}

		return new PlaceDetailResponse(
			placeInfoResponse.id(),
			placeInfoResponse.name(),
			placeInfoResponse.thumbnail(),
			placeInfoResponse.nationalPhoneNumber(),
			placeInfoResponse.internationalPhoneNumber(),
			placeInfoResponse.formattedAddress(),
			Location.from(placeInfoResponse.location()),
			placeInfoResponse.userRatingCount(),
			placeInfoResponse.rating(),
			placeInfoResponse.regularOpeningHours(),
			placeInfoResponse.googleMapsUri(),
			placeInfoResponse.websiteUri(),
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
