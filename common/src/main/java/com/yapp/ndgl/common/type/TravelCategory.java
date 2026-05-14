package com.yapp.ndgl.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TravelCategory {
	FOOD("맛집"),
	CAFE("카페/디저트"),
	HEALING("힐링/풍경"),
	ATTRACTION("명소"),
	LOCAL("로컬"),
	SHOPPING("쇼핑"),
	ACTIVITY("액티비티/체험"),
	BUDGET("가성비");

	private final String label;
}
