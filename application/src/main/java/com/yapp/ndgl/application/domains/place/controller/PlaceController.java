package com.yapp.ndgl.application.domains.place.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yapp.ndgl.application.domains.place.api.PlaceApi;
import com.yapp.ndgl.application.domains.place.controller.response.PlaceDetailResponse;
import com.yapp.ndgl.application.domains.place.controller.response.PlacePhotoResponse;
import com.yapp.ndgl.application.domains.place.facade.PlaceDetailFacade;
import com.yapp.ndgl.application.domains.place.facade.PlacePhotoFacade;
import com.yapp.ndgl.common.response.SuccessResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/places")
public class PlaceController implements PlaceApi {

	private final PlaceDetailFacade placeDetailFacade;
	private final PlacePhotoFacade placePhotoFacade;

	@Override
	@GetMapping("/detail")
	public ResponseEntity<?> readPlaceDetail(
		final @RequestParam("placeId") String googlePlaceId
	) {
		PlaceDetailResponse response = placeDetailFacade.readPlaceDetail(googlePlaceId);
		return ResponseEntity.ok(SuccessResponse.success("place", response));
	}

	@Override
	@GetMapping("/photos")
	public ResponseEntity<?> readPlacePhotos(
		final @RequestParam("placeId") String googlePlaceId
	) {
		PlacePhotoResponse response = placePhotoFacade.readPlacePhotos(googlePlaceId);
		return ResponseEntity.ok(SuccessResponse.success(response));
	}
}
