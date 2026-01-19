package com.yapp.ndgl.application.domains.place.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.place.dto.PlaceDetailResponse;
import com.yapp.ndgl.application.domains.place.dto.PlacePhotoUrisResponse;
import com.yapp.ndgl.application.domains.place.service.PlaceDetailService;
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

	public PlaceDetailResponse readPlaceDetail(final String placeId) {
		// 1. 장소 세부 정보 조회
		GooglePlaceDetailsResponse googlePlaceDetailsResponse = placeDetailService.readPlaceDetail(placeId);

		log.info("response = {}", googlePlaceDetailsResponse);
		// 2. photo URIs 조회 (1개씩 순차 호출) 및 DB 저장
		PlacePhotoUrisResponse photoResponse = placePhotoService.getPhotoUris(placeId, googlePlaceDetailsResponse.photos());

		// 3. 두 응답을 합쳐서 반환
		return PlaceDetailResponse.of(googlePlaceDetailsResponse, photoResponse);
	}
}
