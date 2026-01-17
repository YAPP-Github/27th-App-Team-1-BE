package com.yapp.ndgl.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GoogleMapsErrorCode implements BaseErrorCode {
	/**
	 * GMAP_PLACE-07-xxx
	 * EXTERNAL_SERVICE
	 */
	API_CALL_FAILED(StatusCode.GATEWAY_TIMEOUT, DomainCode.GMAP_PLACE,
		CategoryCode.EXTERNAL_SERVICE, "001", "Google Maps Places API 호출에 실패했습니다"),
	API_TIMEOUT(StatusCode.GATEWAY_TIMEOUT, DomainCode.GMAP_PLACE,
		CategoryCode.EXTERNAL_SERVICE, "002", "Google Maps Places API 응답 시간이 초과되었습니다"),
	INVALID_PLACE_ID(StatusCode.BAD_REQUEST, DomainCode.GMAP_PLACE,
		CategoryCode.EXTERNAL_SERVICE, "003", "유효하지 않은 Place ID 입니다"),
	RESPONSE_PARSE_FAILED(StatusCode.INTERNAL_SERVER_ERROR, DomainCode.GMAP_PLACE,
		CategoryCode.EXTERNAL_SERVICE, "004", "Google Maps Places API 응답 파싱에 실패했습니다");

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
