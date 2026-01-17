package com.yapp.ndgl.application.domains.place.service;

import org.springframework.stereotype.Service;

import com.yapp.ndgl.clients.google.places.GoogleMapsPlaceClient;
import com.yapp.ndgl.clients.google.places.dto.PlaceDetailsRequest;
import com.yapp.ndgl.clients.google.places.dto.PlaceDetailsResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceDetailService {

	private final GoogleMapsPlaceClient googleMapsPlaceClient;

	public PlaceDetailsResponse readPlaceDetail(final String placeId) {
		PlaceDetailsRequest request = PlaceDetailsRequest.of(placeId, "ko");
		return googleMapsPlaceClient.readPlaceDetails(request);
	}
}
