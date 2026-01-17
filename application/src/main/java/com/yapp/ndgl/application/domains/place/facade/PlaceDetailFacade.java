package com.yapp.ndgl.application.domains.place.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.place.service.PlaceDetailService;
import com.yapp.ndgl.clients.google.places.dto.PlaceDetailsResponse;

import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class PlaceDetailFacade {

	private final PlaceDetailService placeDetailService;

	public PlaceDetailsResponse readPlaceDetail(final String placeId) {
		return placeDetailService.readPlaceDetail(placeId);
	}
}
