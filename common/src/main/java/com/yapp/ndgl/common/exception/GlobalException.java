package com.yapp.ndgl.common.exception;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

	private final BaseErrorCode baseErrorCode;

	public GlobalException(final BaseErrorCode baseErrorCode) {
		super(baseErrorCode.getErrorMessage());
		this.baseErrorCode = baseErrorCode;
	}

	public ErrorCausedBy errorCausedBy() {
		return baseErrorCode.errorCausedBy();
	}

	public StatusCode getStatusCode() {
		return baseErrorCode.getStatusCode();
	}

	public String getErrorMessage() {
		return baseErrorCode.getErrorMessage();
	}

}
