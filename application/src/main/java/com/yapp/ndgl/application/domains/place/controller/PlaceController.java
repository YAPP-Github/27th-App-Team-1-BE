package com.yapp.ndgl.application.domains.place.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yapp.ndgl.application.domains.place.api.PlaceApi;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlaceClient;
import com.yapp.ndgl.clients.google.places.dto.PlaceDetailsRequest;
import com.yapp.ndgl.clients.google.places.dto.PlaceDetailsResponse;
import com.yapp.ndgl.common.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/places")
public class PlaceController implements PlaceApi {

	private final GoogleMapsPlaceClient googleMapsPlaceClient;

	@Override
	public ResponseEntity<?> readPlaceDetail(
		final String placeId
	) {
		PlaceDetailsRequest request = PlaceDetailsRequest.of(placeId, "ko");
		PlaceDetailsResponse response = googleMapsPlaceClient.readPlaceDetails(request);

		return ResponseEntity.ok(SuccessResponse.success("place", response));
	}
}
