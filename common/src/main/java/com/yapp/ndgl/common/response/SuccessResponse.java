package com.yapp.ndgl.common.response;

import java.util.Map;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SuccessResponse<T> {

	private final String code = "2000";
	private final String message = "요청에 성공하였습니다.";
	private T data;

	@Builder
	public SuccessResponse(T data) {
		this.data = data;
	}

	public static <T> SuccessResponse<Map<String, T>> noContent() {
		return SuccessResponse.<Map<String, T>>builder()
			.data(Map.of())
			.build();
	}

	public static <T> SuccessResponse<Map<String, T>> of(String key, T data) {
		return SuccessResponse.<Map<String, T>>builder()
			.data(Map.of(key, data))
			.build();
	}
}
