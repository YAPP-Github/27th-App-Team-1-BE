package com.yapp.ndgl.application.domains.place.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.application.domains.place.dto.PlaceInfoResponse;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlaceDetailClient;
import com.yapp.ndgl.clients.google.places.dto.request.PlaceDetailsRequest;
import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceDetailsResponse;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.GoogleMapsErrorCode;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.service.PlaceDomainService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceDetailService {

	private final PlaceDomainService placeDomainService;
	private final GoogleMapsPlaceDetailClient googleMapsPlaceDetailClient;
	private final ObjectMapper objectMapper;

	public PlaceInfoResponse readPlaceDetail(final String placeId) {
		log.info("[GetPlaceDetail] 장소 상세 조회 시작. placeId:{}", placeId);

		// 1. DB 조회
		Place place = placeDomainService.findByPlaceId(placeId).orElse(null);

		if (place != null) {
			log.info("[GetPlaceDetail] DB 조회 성공 후 반환. placeId:{}, id:{}", placeId, place.getId());
			return toPlaceDetailInfoResponse(place);
		}

		// 2. 없으면 구글 조회
		log.info("[GetPlaceDetail] DB에 데이터 없음. Google Maps API 호출 시작. placeId:{}", placeId);
		PlaceDetailsRequest request = PlaceDetailsRequest.of(placeId, "ko");
		GooglePlaceDetailsResponse response = googleMapsPlaceDetailClient.readPlaceDetails(request);

		// 3. 저장 후 반환
		Place savedPlace = placeDomainService.save(toPlace(response));
		log.info("[GetPlaceDetail] DB 저장 완료 후 반환. placeId:{}, id:{}", placeId, savedPlace.getId());
		return PlaceInfoResponse.from(response);
	}

	private PlaceInfoResponse toPlaceDetailInfoResponse(final Place place) {
		try {
			PlaceInfoResponse.Location location = place.getLatitude() != null && place.getLongitude() != null
				? new PlaceInfoResponse.Location(place.getLatitude(), place.getLongitude())
				: null;

			PlaceInfoResponse.RegularOpeningHours regularOpeningHours = null;
			if (place.getRegularOpeningHoursJson() != null) {
				regularOpeningHours = objectMapper.readValue(
					place.getRegularOpeningHoursJson(),
					PlaceInfoResponse.RegularOpeningHours.class
				);
			}

			List<PlaceInfoResponse.PhotoMeta> photos = null;
			if (place.getPhotosJson() != null) {
				photos = objectMapper.readValue(
					place.getPhotosJson(),
					new TypeReference<List<PlaceInfoResponse.PhotoMeta>>() {
					}
				);
			}

			return new PlaceInfoResponse(
				place.getPlaceId(),
				place.getName(),
				place.getNationalPhoneNumber(),
				place.getInternationalPhoneNumber(),
				place.getFormattedAddress(),
				location,
				place.getUserRatingCount(),
				place.getRating(),
				regularOpeningHours,
				place.getGoogleMapsUri(),
				place.getWebsiteUri(),
				photos
			);
		} catch (Exception e) {
			log.error("PlaceDetailInfoResponse 변환 실패: placeId={}", place.getPlaceId(), e);
			throw new GlobalException(GoogleMapsErrorCode.RESPONSE_PARSE_FAILED);
		}
	}

	private Place toPlace(final GooglePlaceDetailsResponse response) {
		try {
			String name = null;
			if (response.name() != null) {
				name = response.name().text();
			}

			String regularOpeningHoursJson = null;
			if (response.regularOpeningHours() != null) {
				regularOpeningHoursJson = objectMapper.writeValueAsString(response.regularOpeningHours());
			}

			String photosJson = null;
			if (response.photos() != null && !response.photos().isEmpty()) {
				photosJson = objectMapper.writeValueAsString(response.photos());
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
				name,
				regularOpeningHoursJson,
				photosJson
			);
		} catch (Exception e) {
			log.error("Place 변환 실패: placeId={}", response.id(), e);
			throw new GlobalException(GoogleMapsErrorCode.RESPONSE_PARSE_FAILED);
		}
	}
}
