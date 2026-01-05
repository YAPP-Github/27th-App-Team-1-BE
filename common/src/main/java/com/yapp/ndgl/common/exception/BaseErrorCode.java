package com.yapp.ndgl.common.exception;

public interface BaseErrorCode {
	ErrorCausedBy errorCausedBy();

	StatusCode getStatusCode();

	String getErrorMessage();
}
