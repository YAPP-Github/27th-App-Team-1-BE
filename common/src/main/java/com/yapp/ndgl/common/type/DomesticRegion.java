package com.yapp.ndgl.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DomesticRegion {
	UNDEFINED("미지정");

	private final String label;
}
