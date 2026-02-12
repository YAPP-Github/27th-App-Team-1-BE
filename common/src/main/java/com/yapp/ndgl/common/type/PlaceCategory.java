package com.yapp.ndgl.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlaceCategory {
	AIRPORT("공항"),
	TRANSPORT("교통수단"),
	ATTRACTION("관광명소"),
	RESTAURANT("음식점"),
	CAFE("카페"),
	ACCOMMODATION("숙소");

	private final String label;
}
