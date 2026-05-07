package com.yapp.ndgl.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TravelCategory {
	UNCATEGORIZED("미분류");

	private final String label;
}
