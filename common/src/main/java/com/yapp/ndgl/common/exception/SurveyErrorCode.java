package com.yapp.ndgl.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SurveyErrorCode implements BaseErrorCode {
	/**
	 * SURVEY-02-xxx
	 * RESOURCE_NOT_FOUND
	 */
	NOT_FOUND_ONBOARDING_SURVEY(StatusCode.NOT_FOUND, DomainCode.SURVEY,
		CategoryCode.RESOURCE_NOT_FOUND, "001", "온보딩 설문조사를 찾을 수 없습니다"),

	/**
	 * SURVEY-03-xxx
	 * RESOURCE_CONFLICT
	 */
	ALREADY_EXISTS_ONBOARDING_SURVEY(StatusCode.CONFLICT, DomainCode.SURVEY,
		CategoryCode.RESOURCE_CONFLICT, "001", "이미 온보딩 설문조사가 존재합니다");

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
