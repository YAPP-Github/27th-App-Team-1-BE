package com.yapp.ndgl.common.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuggestionStatus {
	PENDING("검토 대기"),
	ACCEPTED("승인됨"),
	DENIED("반려됨");

	private final String label;
}
