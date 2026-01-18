package com.yapp.ndgl.application.domains.place.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yapp.ndgl.application.domains.place.api.PlaceApi;
import com.yapp.ndgl.application.domains.place.facade.PlaceDetailFacade;
import com.yapp.ndgl.application.domains.place.facade.PlacePhotoFacade;
import com.yapp.ndgl.clients.google.places.dto.response.PlaceDetailsResponse;
import com.yapp.ndgl.clients.google.places.dto.response.PlacePhotoResponse;
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
		final @RequestParam("placeId") String placeId
	) {
		PlaceDetailsResponse response = placeDetailFacade.readPlaceDetail(placeId);
		return ResponseEntity.ok(SuccessResponse.success("place", response));
	}

	@GetMapping("/photos")
	public ResponseEntity<?> getPlacePhoto(
		final @RequestParam("photoName") String photoName
	) {
		PlacePhotoResponse response = placePhotoFacade.getPlacePhoto(photoName);
		return ResponseEntity.ok(SuccessResponse.success("place", response));
	}


}
