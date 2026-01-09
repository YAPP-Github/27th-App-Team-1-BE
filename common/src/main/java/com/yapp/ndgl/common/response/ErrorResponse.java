package com.yapp.ndgl.common.response;

import java.util.Collections;
import java.util.List;

import com.yapp.ndgl.common.exception.BaseErrorCode;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse <T> {

	private String code;
	private String message;
	private T errors;

	public static ErrorResponse<List<Object>> error(final BaseErrorCode baseErrorCode) {
		String errorCode = baseErrorCode.errorCausedBy().getErrorCode();
		String message = baseErrorCode.getErrorMessage();

		return ErrorResponse.<List<Object>>builder()
			.message(message)
			.code(errorCode)
			.errors(Collections.emptyList())
			.build();
	}

	public static <T> ErrorResponse<T> error(final BaseErrorCode baseErrorCode, final T errors) {
		String errorCode = baseErrorCode.errorCausedBy().getErrorCode();
		String message = baseErrorCode.getErrorMessage();

		return ErrorResponse.<T>builder()
			.message(message)
			.code(errorCode)
			.errors(errors)
			.build();
	}
}
