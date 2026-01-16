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
	private SuccessResponse(final T data) {
		this.data = data;
	}

	public static <T> SuccessResponse success(final T data) {
		return SuccessResponse.builder()
				.data(data)
				.build();
	}

	public static <T> SuccessResponse<Map<String, T>> success(final String key, final T data) {
		return SuccessResponse.<Map<String, T>>builder()
			.data(Map.of(key, data))
			.build();
	}

	public static SuccessResponse<Map<String, Object>> noContent() {
		return SuccessResponse.<Map<String, Object>>builder()
			.data(Map.of())
			.build();
	}
}
