package com.yapp.ndgl.application.domains.place.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yapp.ndgl.application.domains.place.dto.PlacePhotoUrisResponse;
import com.yapp.ndgl.clients.google.places.GoogleMapsPlacePhotoClient;
import com.yapp.ndgl.clients.google.places.dto.request.PlacePhotoRequest;
import com.yapp.ndgl.clients.google.places.dto.response.GooglePlaceDetailsResponse;
import com.yapp.ndgl.clients.google.places.dto.response.GooglePlacePhotoResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlacePhotoService {

	private final GoogleMapsPlacePhotoClient googleMapsPlacePhotoClient;

	public GooglePlacePhotoResponse getPlacePhoto(
		final String photoName,
		final int maxHeightPx,
		final int maxWidthPx
	) {
		PlacePhotoRequest request = PlacePhotoRequest.of(photoName, maxHeightPx, maxWidthPx);
		return googleMapsPlacePhotoClient.getPhotoUri(request);
	}

	/**
	 * photos 목록에서 각 photo의 URI를 1개씩 조회하여 반환한다.
	 *
	 * @param photos GooglePlaceDetailsResponse의 photo 목록
	 * @return photo URI가 포함된 응답
	 */
	public PlacePhotoUrisResponse getPhotoUris(final List<GooglePlaceDetailsResponse.Photo> photos) {
		if (photos == null || photos.isEmpty()) {
			return PlacePhotoUrisResponse.empty();
		}

		List<PlacePhotoUrisResponse.PhotoUri> photoUris = photos.stream()
			.map(this::fetchPhotoUri)
			.toList();

		return PlacePhotoUrisResponse.from(photoUris);
	}

	private PlacePhotoUrisResponse.PhotoUri fetchPhotoUri(final GooglePlaceDetailsResponse.Photo photo) {
		PlacePhotoRequest request = PlacePhotoRequest.of(
			photo.name(),
			photo.heightPx(),
			photo.widthPx()
		);

		GooglePlacePhotoResponse response = googleMapsPlacePhotoClient.getPhotoUri(request);

		return PlacePhotoUrisResponse.PhotoUri.of(
			photo.name(),
			photo.widthPx(),
			photo.heightPx(),
			response.uri()
		);
	}
}
