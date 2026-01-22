package com.yapp.ndgl.application.domains.place.controller.response;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.domain.place.Place;

/**
 * 장소 세부 정보 Service 레이어 응답 DTO.
 */
public record PlaceInfoResponse(
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
	List<PhotoMeta> photos
) {


	public static PlaceInfoResponse from(final Place place, final ObjectMapper objectMapper) {
		try {

			Location location = Location.of(place.getLatitude(), place.getLongitude());

			List<PhotoMeta> photoMetas = null;
			if (place.getPhotosJson() != null) {
				photoMetas = objectMapper.readValue(
					place.getPhotosJson(),
					new TypeReference<>() {}
				);
			}

			List<String> regularOpeningHours = null;
			if (place.getRegularOpeningHours() != null) {
				regularOpeningHours = objectMapper.readValue(
					place.getRegularOpeningHours(),
					new TypeReference<>() {}
				);
			}

			return new PlaceInfoResponse(
				place.getGooglePlaceId(),
				place.getName(),
				place.getThumbnail(),
				place.getNationalPhoneNumber(),
				place.getInternationalPhoneNumber(),
				place.getFormattedAddress(),
				location,
				place.getUserRatingCount(),
				place.getRating(),
				regularOpeningHours,
				place.getGoogleMapsUri(),
				place.getWebsiteUri(),
				photoMetas
			);
		} catch (Exception e) {
			throw new RuntimeException("PlaceInfoResponse 변환 실패: googlePlaceId=" + place.getGooglePlaceId(), e);
		}
	}

	public record Location(Double latitude, Double longitude) {
		public static Location of(final Double latitude, final Double longitude) {

			if (latitude == null || longitude == null) {
				return null;
			}

			return new Location(latitude, longitude);
		}
	}

	public record PhotoMeta(
		String name,
		Integer widthPx,
		Integer heightPx
	) {
	}
}
