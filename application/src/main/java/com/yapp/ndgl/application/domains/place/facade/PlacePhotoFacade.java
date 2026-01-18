package com.yapp.ndgl.application.domains.place.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.place.service.PlacePhotoService;
import com.yapp.ndgl.clients.google.places.dto.response.PlacePhotoResponse;

import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class PlacePhotoFacade {

	private final PlacePhotoService placePhotoService;

	public PlacePhotoResponse getPlacePhoto(final String photoName) {
		// Todo: photoName, maxHeightPx, maxWidthPx -> PlaceDetailResponse 에서 추출 필요
		int maxHeightPx = 3024;
		int maxWidthPx = 3024;
		return placePhotoService.getPlacePhoto(photoName, maxHeightPx, maxWidthPx);
	}
}
