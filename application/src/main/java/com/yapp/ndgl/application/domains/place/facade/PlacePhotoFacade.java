package com.yapp.ndgl.application.domains.place.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.place.controller.response.PlacePhotoResponse;
import com.yapp.ndgl.application.domains.place.service.PlacePhotoServiceV2;

import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class PlacePhotoFacade {

	private final PlacePhotoServiceV2 placePhotoServiceV2;

	public PlacePhotoResponse readPlacePhotos(final String googlePlaceId) {
		return placePhotoServiceV2.readPlacePhotoUris(googlePlaceId);
	}
}
