package com.yapp.ndgl.application.domains.place.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.application.domains.place.controller.response.PlaceDetailResponse;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlaceDetailClient;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlacePhotoClient;
import com.yapp.ndgl.clients.google.places.dto.request.PlaceDetailsRequest;
import com.yapp.ndgl.clients.google.places.dto.request.PlacePhotoRequest;
import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceDetailsResponse;
import com.yapp.ndgl.application.domains.place.mapper.GooglePlaceTypeMapper;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.GoogleMapsErrorCode;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.place.PlaceCategory;
import com.yapp.ndgl.domain.place.service.PlaceDomainService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceDetailService {

	private final PlaceDomainService placeDomainService;
	private final GoogleMapsPlaceDetailClient googleMapsPlaceDetailClient;
	private final GoogleMapsPlacePhotoClient googleMapsPlacePhotoClient;
	private final ObjectMapper objectMapper;

	/**
	 * DB에서 장소 정보 조회 (조회만 수행, 부수효과 없음)
	 */
	public PlaceDetailResponse readPlaceDetailFromDB(final String googlePlaceId) {
		log.info("[GetPlaceDetailFromDb] DB에서 장소 조회 시작. googlePlaceId:{}", googlePlaceId);

		Place place = placeDomainService.readPlaceDetailByGooglePLaceId(googlePlaceId);

		log.info("[GetPlaceDetailFromDb] DB 조회 성공. googlePlaceId:{}, id:{}", googlePlaceId, place.getId());
		return PlaceDetailResponse.toResponse(place, objectMapper);
	}

	/**
	 * Google Maps API에서 장소 검색 후 DB에 저장
	 */
	public GooglePlaceDetailsResponse searchPlaceFromGoogleMaps(final String googlePlaceId) {
		log.info("[SearchAndSavePlace] Google Maps API 호출 시작. googlePlaceId:{}", googlePlaceId);

		// 이미 존재하면 Google API 호출 전에 차단
		placeDomainService.validateNotExistsByGooglePlaceId(googlePlaceId);

		// 1. Google Maps API 호출
		PlaceDetailsRequest request = PlaceDetailsRequest.of(googlePlaceId, "ko");
		return googleMapsPlaceDetailClient.readPlaceDetails(request);

	}

	public PlaceDetailResponse savePlace(final GooglePlaceDetailsResponse response) {
		// 2. 썸네일 조회
		String thumbnail = null;
		if (response.photos() != null && !response.photos().isEmpty()) {
			GooglePlaceDetailsResponse.PhotoMeta photoMeta = response.photos().get(0);
			PlacePhotoRequest photoRequest = PlacePhotoRequest.of(photoMeta.name(), photoMeta.heightPx(),
				photoMeta.widthPx());
			thumbnail = googleMapsPlacePhotoClient.getPhotoUri(photoRequest).uri();
		}

		// 3. DB 저장 후 반환
		Place savedPlace = placeDomainService.save(toPlace(response, thumbnail));
		log.info("[SearchAndSavePlace] DB 저장 완료. googlePlaceId:{}, id:{}", response.id(), savedPlace.getId());
		return PlaceDetailResponse.toResponse(savedPlace, objectMapper);
	}

	private Place toPlace(final GooglePlaceDetailsResponse response, final String thumbnail) {
		try {
			String name = null;
			if (response.name() != null) {
				name = response.name().text();
			}

			String regularOpeningHours = null;
			if (response.regularOpeningHours() != null) {
				regularOpeningHours = objectMapper.writeValueAsString(
					response.regularOpeningHours().regularOpeningHours()
				);
			}

			String photosJson = null;
			if (response.photos() != null && !response.photos().isEmpty()) {
				photosJson = objectMapper.writeValueAsString(response.photos());
			}

			Double latitude = response.location() != null ? response.location().latitude() : null;
			Double longitude = response.location() != null ? response.location().longitude() : null;

			String priceCurrencyCode = null;
			String priceStartUnits = null;
			String priceEndUnits = null;
			if (response.priceRange() != null) {
				if (response.priceRange().startPrice() != null) {
					priceCurrencyCode = response.priceRange().startPrice().currencyCode();
					priceStartUnits = response.priceRange().startPrice().units();
				}
				if (response.priceRange().endPrice() != null) {
					priceEndUnits = response.priceRange().endPrice().units();
				}
			}

			PlaceCategory category = GooglePlaceTypeMapper.toCategory(response.primaryType(), response.types());

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
				thumbnail,
				regularOpeningHours,
				photosJson,
				priceCurrencyCode,
				priceStartUnits,
				priceEndUnits,
				category
			);
		} catch (Exception e) {
			log.error("Place 변환 실패: googlePlaceId={}", response.id(), e);
			throw new GlobalException(GoogleMapsErrorCode.RESPONSE_PARSE_FAILED);
		}
	}
}
