package com.yapp.ndgl.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommonErrorCode implements BaseErrorCode {
	/**
	 * COMM-01-xxx
	 * INPUT_VALIDATION
	 */

	MISSING_REQUEST_HEADER(StatusCode.BAD_REQUEST, DomainCode.COMM,
		CategoryCode.INPUT_VALIDATION, "001", "필수 헤더가 존재하지 않습니다"),
	TYPE_MISS_MATCH_ERRORS_IN_REQUEST_BODY(StatusCode.BAD_REQUEST, DomainCode.COMM,
		CategoryCode.INPUT_VALIDATION, "002", "허용되지 않는 필드입니다."),
	TYPE_MISS_MATCH_ERRORS_IN_REQUEST_PARAM_DATA(StatusCode.BAD_REQUEST, DomainCode.COMM,
		CategoryCode.INPUT_VALIDATION, "003", "허용되지 않는 쿼리입니다."),
	METHOD_NOT_ALLOWED(StatusCode.METHOD_NOT_ALLOWED, DomainCode.COMM,
		CategoryCode.INPUT_VALIDATION, "004", "요청하신 HTTP 메서드는 이 리소스에서 지원되지 않습니다."),

	VALIDATION_ERRORS_IN_REQUEST_DATA(StatusCode.UNPROCESSABLE_ENTITY, DomainCode.COMM,
		CategoryCode.INPUT_VALIDATION, "005", "유효성 검증에 실패하였습니다"),

	/**
	 * COMM-02-xxx
	 * RESOURCE_NOT_FOUND
	 */

	/**
	 * COMM-03-xxx
	 * RESOURCE_CONFLICT
	 */

	/**
	 * COMM-04-xxx
	 * BUSINESS_RULE_VIOLATION
	 */
	TOO_MANY_REQUESTED(StatusCode.TOO_MANY_REQUESTS, DomainCode.COMM,
		CategoryCode.BUSINESS_RULE_VIOLATION, "001", "너무 많은 요청이 감지되었습니다. 잠시 후 다시 시도하세요."),

	/**
	 * COMM-05-xxx
	 * AUTHENTICATION
	 */

	/**
	 * COMM-06-xxx
	 * AUTHORIZATION
	 */

	/**
	 * COMM-07-xxx
	 * EXTERNAL_SERVICE
	 */

	/**
	 * COMM-08-xxx
	 * INTERNAL_SYSTEM
	 */

	;

	private final StatusCode statusCode;
	private final DomainCode domainCode;
	private final CategoryCode categoryCode;
	private final String detailCode;
	private final String message;

	@Override
	public ErrorCausedBy errorCausedBy() {
		return ErrorCausedBy.of(domainCode, categoryCode, detailCode);
	}

	@Override
	public StatusCode getStatusCode() {
		return statusCode;
	}

	@Override
	public String getErrorMessage() {
		return message;
	}
}
