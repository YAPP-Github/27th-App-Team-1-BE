package com.yapp.ndgl.application.domains.place.dto;

import java.util.List;

import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceDetailsResponse;

/**
 * 장소 세부 정보 최종 응답 DTO.
 * GooglePlaceDetailsResponse와 PlacePhotoUrisResponse를 합친 결과.
 */
public record PlaceDetailResponse(
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

	public record Location(Double latitude, Double longitude) {

		public static Location from(final GooglePlaceDetailsResponse.Location location) {
			if (location == null) {
				return null;
			}
			return new Location(location.latitude(), location.longitude());
		}
	}

	public record DisplayName(String text, String languageCode) {

		public static DisplayName from(final GooglePlaceDetailsResponse.DisplayName displayName) {
			if (displayName == null) {
				return null;
			}
			return new DisplayName(displayName.text(), displayName.languageCode());
		}
	}

	public record RegularOpeningHours(Boolean openNow, List<Period> periods, List<String> weekdayDescriptions) {

		public static RegularOpeningHours from(final GooglePlaceDetailsResponse.RegularOpeningHours regularOpeningHours) {
			if (regularOpeningHours == null) {
				return null;
			}
			List<Period> periods = regularOpeningHours.periods() != null
				? regularOpeningHours.periods().stream().map(Period::from).toList()
				: null;
			return new RegularOpeningHours(regularOpeningHours.openNow(), periods, regularOpeningHours.weekdayDescriptions());
		}
	}

	public record Period(DayTime open, DayTime close) {

		public static Period from(final GooglePlaceDetailsResponse.Period period) {
			if (period == null) {
				return null;
			}
			return new Period(DayTime.from(period.open()), DayTime.from(period.close()));
		}
	}

	public record DayTime(Integer day, Integer hour, Integer minute) {

		public static DayTime from(final GooglePlaceDetailsResponse.DayTime dayTime) {
			if (dayTime == null) {
				return null;
			}
			return new DayTime(dayTime.day(), dayTime.hour(), dayTime.minute());
		}
	}

	public record Photo(
		String name,
		Integer widthPx,
		Integer heightPx,
		List<AuthorAttribution> authorAttributions,
		String flagContentUri,
		String googleMapsUri,
		String photoUri
	) {

		public static Photo from(
			final GooglePlaceDetailsResponse.Photo googlePhoto,
			final String photoUri
		) {
			if (googlePhoto == null) {
				return null;
			}
			List<AuthorAttribution> attributions = googlePhoto.authorAttributions() != null
				? googlePhoto.authorAttributions().stream().map(AuthorAttribution::from).toList()
				: null;
			return new Photo(
				googlePhoto.name(),
				googlePhoto.widthPx(),
				googlePhoto.heightPx(),
				attributions,
				googlePhoto.flagContentUri(),
				googlePhoto.googleMapsUri(),
				photoUri
			);
		}
	}

	public record AuthorAttribution(String displayName, String uri, String photoUri) {

		public static AuthorAttribution from(final GooglePlaceDetailsResponse.AuthorAttribution attribution) {
			if (attribution == null) {
				return null;
			}
			return new AuthorAttribution(attribution.displayName(), attribution.uri(), attribution.photoUri());
		}
	}

	public record PostalAddress(
		String regionCode,
		String languageCode,
		String postalCode,
		String administrativeArea,
		List<String> addressLines
	) {

		public static PostalAddress from(final GooglePlaceDetailsResponse.PostalAddress postalAddress) {
			if (postalAddress == null) {
				return null;
			}
			return new PostalAddress(
				postalAddress.regionCode(),
				postalAddress.languageCode(),
				postalAddress.postalCode(),
				postalAddress.administrativeArea(),
				postalAddress.addressLines()
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
					return Photo.from(googlePhoto, photoUri);
				})
				.toList();
		}

		return new PlaceDetailResponse(
			googleResponse.id(),
			googleResponse.nationalPhoneNumber(),
			googleResponse.internationalPhoneNumber(),
			googleResponse.formattedAddress(),
			Location.from(googleResponse.location()),
			googleResponse.rating(),
			googleResponse.googleMapsUri(),
			googleResponse.websiteUri(),
			RegularOpeningHours.from(googleResponse.regularOpeningHours()),
			googleResponse.userRatingCount(),
			DisplayName.from(googleResponse.displayName()),
			photos,
			PostalAddress.from(googleResponse.postalAddress())
		);
	}

	private static String findPhotoUri(final String photoName, final PlacePhotoUrisResponse photoResponse) {
		if (photoResponse == null || photoResponse.photoUris() == null) {
			return null;
		}
		return photoResponse.photoUris().stream()
			.filter(p -> p.name().equals(photoName))
			.findFirst()
			.map(PlacePhotoUrisResponse.PhotoUri::photoUri)
			.orElse(null);
	}
}
