package com.yapp.ndgl.application.domains.place.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlaceClient;
import com.yapp.ndgl.clients.google.places.dto.PlaceDetailsRequest;
import com.yapp.ndgl.clients.google.places.dto.PlaceDetailsResponse;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.GoogleMapsErrorCode;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.PlaceDomainService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceDetailService {

	private final PlaceDomainService placeDomainService;
	private final GoogleMapsPlaceClient googleMapsPlaceClient;
	private final ObjectMapper objectMapper;

	public PlaceDetailsResponse readPlaceDetail(final String placeId) {
		log.info("[GetPlaceDetail] 장소 상세 조회 시작. placeId:{}", placeId);

		// 1. DB 조회
		Place place = placeDomainService.findByPlaceId(placeId).orElse(null);

		if (place != null) {
			log.info("[GetPlaceDetail] DB 조회 성공 후 반환. placeId:{}, id:{}", placeId, place.getId());
			return toPlaceDetailsResponse(place);
		}

		// 2. 없으면 구글 조회
		log.info("[GetPlaceDetail] DB에 데이터 없음. Google Maps API 호출 시작. placeId:{}", placeId);
		PlaceDetailsRequest request = PlaceDetailsRequest.of(placeId, "ko");
		PlaceDetailsResponse response = googleMapsPlaceClient.readPlaceDetails(request);

		// 3. 저장 후 반환
		Place savedPlace = placeDomainService.save(toPlace(response));
		log.info("[GetPlaceDetail] DB 저장 완료 후 반환. placeId:{}, id:{}", placeId, savedPlace.getId());
		return response;
	}

	private PlaceDetailsResponse toPlaceDetailsResponse(Place place) {
		try {
			PlaceDetailsResponse.Location location = place.getLatitude() != null && place.getLongitude() != null
				? new PlaceDetailsResponse.Location(place.getLatitude(), place.getLongitude())
				: null;

			PlaceDetailsResponse.DisplayName displayName = null;
			if (place.getDisplayNameJson() != null) {
				displayName = objectMapper.readValue(place.getDisplayNameJson(), PlaceDetailsResponse.DisplayName.class);
			}

			PlaceDetailsResponse.RegularOpeningHours regularOpeningHours = null;
			if (place.getRegularOpeningHoursJson() != null) {
				regularOpeningHours = objectMapper.readValue(
					place.getRegularOpeningHoursJson(),
					PlaceDetailsResponse.RegularOpeningHours.class
				);
			}

			List<PlaceDetailsResponse.Photo> photos = null;
			if (place.getPhotosJson() != null) {
				photos = objectMapper.readValue(
					place.getPhotosJson(),
					new TypeReference<List<PlaceDetailsResponse.Photo>>() {
					}
				);
			}

			PlaceDetailsResponse.PostalAddress postalAddress = null;
			if (place.getPostalAddressJson() != null) {
				postalAddress = objectMapper.readValue(
					place.getPostalAddressJson(),
					PlaceDetailsResponse.PostalAddress.class
				);
			}

			return new PlaceDetailsResponse(
				place.getPlaceId(),
				place.getNationalPhoneNumber(),
				place.getInternationalPhoneNumber(),
				place.getFormattedAddress(),
				location,
				place.getRating(),
				place.getGoogleMapsUri(),
				place.getWebsiteUri(),
				regularOpeningHours,
				place.getUserRatingCount(),
				displayName,
				photos,
				postalAddress
			);
		} catch (Exception e) {
			log.error("PlaceDetailsResponse 변환 실패: placeId={}", place.getPlaceId(), e);
			throw new GlobalException(GoogleMapsErrorCode.RESPONSE_PARSE_FAILED);
		}
	}

	private Place toPlace(final PlaceDetailsResponse response) {
		try {
			String displayNameJson = null;
			if (response.displayName() != null) {
				displayNameJson = objectMapper.writeValueAsString(response.displayName());
			}

			String regularOpeningHoursJson = null;
			if (response.regularOpeningHours() != null) {
				regularOpeningHoursJson = objectMapper.writeValueAsString(response.regularOpeningHours());
			}

			String photosJson = null;
			if (response.photos() != null && !response.photos().isEmpty()) {
				photosJson = objectMapper.writeValueAsString(response.photos());
			}

			String postalAddressJson = null;
			if (response.postalAddress() != null) {
				postalAddressJson = objectMapper.writeValueAsString(response.postalAddress());
			}

			Double latitude = response.location() != null ? response.location().latitude() : null;
			Double longitude = response.location() != null ? response.location().longitude() : null;

			return Place.create(
				response.id(),
				response.formattedAddress(),
				latitude,
				longitude,
				response.rating(),
				response.nationalPhoneNumber(),
				response.internationalPhoneNumber(),
				response.websiteUri(),
				response.googleMapsUri(),
				response.userRatingCount(),
				displayNameJson,
				regularOpeningHoursJson,
				photosJson,
				postalAddressJson
			);
		} catch (Exception e) {
			log.error("Place 변환 실패: placeId={}", response.id(), e);
			throw new GlobalException(GoogleMapsErrorCode.RESPONSE_PARSE_FAILED);
		}
	}
}
