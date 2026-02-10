package com.yapp.ndgl.domain.place;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PlaceCategory {
	AIRPORT("공항"),
	TRANSPORT("교통수단"),
	ATTRACTION("관광명소"),
	RESTAURANT("음식점"),
	CAFE("카페"),
	ACCOMMODATION("숙소");

	private final String label;

	public String getLabel() {
		return label;
	}
}
