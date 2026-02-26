package com.yapp.ndgl.application.domains.place.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.place.controller.response.PlaceDetailResponse;
import com.yapp.ndgl.application.domains.place.service.PlaceDetailService;
import com.yapp.ndgl.application.domains.place.service.PlaceNearbyService;
import com.yapp.ndgl.application.domains.place.service.PlacePhotoService;
import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceDetailsResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Facade
@RequiredArgsConstructor
public class PlaceDetailFacade {

	private final PlaceDetailService placeDetailService;
	private final PlacePhotoService placePhotoService;
	private final PlaceNearbyService placeNearbyService;

	/**
	 * DB에서 장소 상세 정보 조회 (조회만 수행, RESTful GET)
	 * nearbyPlacesJson이 없으면 비동기로 채운다 (다음 조회부터 반환됨)
	 */
	public PlaceDetailResponse getPlaceDetailFromDb(final String googlePlaceId) {
		log.info("DB에서 장소 상세 정보 조회. googlePlaceId={}", googlePlaceId);

		PlaceDetailResponse response = placeDetailService.readPlaceDetailFromDB(googlePlaceId);

		if (response.nearbyPlaces() == null) {
			placeNearbyService.saveNearbyPlacesIfNotExists(googlePlaceId);
		}

		return response;
	}

	/**
	 * Google Maps에서 장소 검색 후 DB에 저장 (생성, RESTful POST)
	 */
	public PlaceDetailResponse searchAndSavePlace(final String googlePlaceId) {
		log.info("Google Maps에서 장소 검색 및 저장 시작. googlePlaceId={}", googlePlaceId);

		// 1. Google Maps API 호출 후 장소 정보 저장
		GooglePlaceDetailsResponse googlePlaceDetailsResponse = placeDetailService.searchPlaceFromGoogleMaps(
			googlePlaceId);

		PlaceDetailResponse placeDetailResponse = placeDetailService.savePlace(googlePlaceDetailsResponse);

		// 2. 사진 및 인근 장소 비동기 저장
		// placePhotoService.savePhotosIfNotExists(googlePlaceId);
		placeNearbyService.saveNearbyPlacesIfNotExists(googlePlaceId);

		log.info("장소 검색 및 저장 완료. googlePlaceId={}", googlePlaceId);

		return placeDetailResponse;
	}
}
