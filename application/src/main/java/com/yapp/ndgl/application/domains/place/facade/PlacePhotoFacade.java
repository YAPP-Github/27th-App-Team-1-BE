package com.yapp.ndgl.application.domains.place.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.place.controller.response.PlacePhotoResponse;
import com.yapp.ndgl.application.domains.place.service.PlacePhotoService;

import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class PlacePhotoFacade {

	private final PlacePhotoService placePhotoService;

	public PlacePhotoResponse readPlacePhotos(final String googlePlaceId) {
		return placePhotoService.readPlacePhotoUris(googlePlaceId);
	}
}
