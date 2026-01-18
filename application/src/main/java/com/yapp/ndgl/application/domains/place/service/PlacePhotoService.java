package com.yapp.ndgl.application.domains.place.service;

import org.springframework.stereotype.Service;

import com.yapp.ndgl.clients.google.places.GoogleMapsPlacePhotoClient;
import com.yapp.ndgl.clients.google.places.dto.request.PlacePhotoRequest;
import com.yapp.ndgl.clients.google.places.dto.response.PlacePhotoResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlacePhotoService {

	private final GoogleMapsPlacePhotoClient googleMapsPlacePhotoClient;

	public PlacePhotoResponse getPlacePhoto(final String photoName, final int maxHeightPx, final int maxWidthPx) {

		PlacePhotoRequest request = PlacePhotoRequest.of(photoName,
			maxHeightPx, maxWidthPx);
		return googleMapsPlacePhotoClient.getPhotoUri(request);
	}
}
