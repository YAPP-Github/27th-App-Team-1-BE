package com.yapp.ndgl.application.domains.place.facade;

import org.springframework.stereotype.Component;

import com.yapp.ndgl.application.domains.place.service.PlaceDetailService;
import com.yapp.ndgl.clients.google.places.dto.PlaceDetailsResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlaceDetailFacade {

	private final PlaceDetailService placeDetailService;

	public PlaceDetailsResponse readPlaceDetail(final String placeId) {
		return placeDetailService.readPlaceDetail(placeId);
	}
}
