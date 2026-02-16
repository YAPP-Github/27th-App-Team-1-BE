package com.yapp.ndgl.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum YouTubeErrorCode implements BaseErrorCode {
	/**
	 * YOUTUBE-07-xxx
	 * EXTERNAL_SERVICE
	 */
	API_CALL_FAILED(StatusCode.GATEWAY_TIMEOUT, DomainCode.YOUTUBE,
		CategoryCode.EXTERNAL_SERVICE, "001", "YouTube Data API 호출에 실패했습니다"),
	API_TIMEOUT(StatusCode.GATEWAY_TIMEOUT, DomainCode.YOUTUBE,
		CategoryCode.EXTERNAL_SERVICE, "002", "YouTube Data API 응답 시간이 초과되었습니다"),

	/**
	 * YOUTUBE-01-xxx
	 * INPUT_VALIDATION
	 */
	INVALID_VIDEO_URL(StatusCode.BAD_REQUEST, DomainCode.YOUTUBE,
		CategoryCode.INPUT_VALIDATION, "001", "유효하지 않은 YouTube 영상 URL입니다"),

	/**
	 * YOUTUBE-02-xxx
	 * RESOURCE_NOT_FOUND
	 */
	VIDEO_NOT_FOUND(StatusCode.NOT_FOUND, DomainCode.YOUTUBE,
		CategoryCode.RESOURCE_NOT_FOUND, "001", "YouTube 영상을 찾을 수 없습니다"),
	CHANNEL_NOT_FOUND(StatusCode.NOT_FOUND, DomainCode.YOUTUBE,
		CategoryCode.RESOURCE_NOT_FOUND, "002", "YouTube 채널을 찾을 수 없습니다");

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
