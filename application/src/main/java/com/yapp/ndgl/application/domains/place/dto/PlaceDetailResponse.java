package com.yapp.ndgl.application.domains.place.dto;

import java.util.List;

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

		public static Location of(final Double latitude, final Double longitude) {
			return new Location(
				latitude,
				longitude
			);
		}
	}

	public record Photo(
		Integer widthPx,
		Integer heightPx,
		String photoUri
	) {

		public static Photo of(
			final Integer widthPx,
			final Integer heightPx,
			final String photoUri
		) {
			return new Photo(
				widthPx,
				heightPx,
				photoUri
			);
		}
	}

	public static PlaceDetailResponse of(
		final PlaceInfoResponse placeInfoResponse,
		final PlacePhotoUrisResponse photoResponse
	) {

		Location location = Location.of(placeInfoResponse.location().latitude(),
			placeInfoResponse.location().longitude());

		List<Photo> photos = photoResponse.photoUris().stream()
			.map(uri ->
				Photo.of(
					uri.widthPx(),
					uri.heightPx(),
					uri.photoUri()
				)).toList();

		return new PlaceDetailResponse(
			placeInfoResponse.id(),
			placeInfoResponse.name(),
			placeInfoResponse.thumbnail(),
			placeInfoResponse.nationalPhoneNumber(),
			placeInfoResponse.internationalPhoneNumber(),
			placeInfoResponse.formattedAddress(),
			location,
			placeInfoResponse.userRatingCount(),
			placeInfoResponse.rating(),
			placeInfoResponse.regularOpeningHours(),
			placeInfoResponse.googleMapsUri(),
			placeInfoResponse.websiteUri(),
			photos
		);
	}
}
