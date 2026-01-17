package com.yapp.ndgl.common.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DomainCode {
	COMM("공통 에러"),
	GMAP_PLACE("Google Maps Place 에러"),
	USER("유저 도메인 에러");

	private final String name;
}
