package com.yapp.ndgl.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TravelErrorCode implements BaseErrorCode {
    /**
     * TRAVEL-02-xxx
     * RESOURCE_NOT_FOUND
     */
    NOT_FOUND_TRAVEL_TEMPLATE(StatusCode.NOT_FOUND, DomainCode.TRAVEL,
        CategoryCode.RESOURCE_NOT_FOUND, "001", "여행 템플릿을 찾을 수 없습니다"),
    NOT_FOUND_USER_TRAVEL(StatusCode.NOT_FOUND, DomainCode.TRAVEL,
        CategoryCode.RESOURCE_NOT_FOUND, "002", "내 여행 정보를 찾을 수 없습니다"),

    /**
     * TRAVEL-03-xxx
     * RESOURCE_CONFLICT
     */
    ALREADY_EXISTS_TRAVEL_PROGRAM(StatusCode.CONFLICT, DomainCode.TRAVEL,
        CategoryCode.RESOURCE_CONFLICT, "001", "이미 존재하는 여행 프로그램입니다"),
    ALREADY_EXISTS_TRAVEL_TEMPLATE(StatusCode.CONFLICT, DomainCode.TRAVEL,
        CategoryCode.RESOURCE_CONFLICT, "002", "이미 저장된 영상입니다"),
    ALREADY_EXISTS_SUGGESTED_TEMPLATE(StatusCode.CONFLICT, DomainCode.TRAVEL,
        CategoryCode.RESOURCE_CONFLICT, "003", "다른 사용자가 이미 요청한 영상입니다."),
    ALREADY_REQUESTED_SUGGESTED_TEMPLATE(StatusCode.CONFLICT, DomainCode.TRAVEL,
        CategoryCode.RESOURCE_CONFLICT, "004", "이미 요청한 영상입니다"),

    /**
     * TRAVEL-04-xxx
     * BUSINESS_RULE_VIOLATION
     */
    INVALID_DATE_ORDER(StatusCode.BAD_REQUEST, DomainCode.TRAVEL,
        CategoryCode.BUSINESS_RULE_VIOLATION, "001", "여행 종료일이 시작일보다 앞설 수 없습니다"),
    INVALID_ITINERARY_REQUEST(StatusCode.BAD_REQUEST, DomainCode.TRAVEL,
        CategoryCode.BUSINESS_RULE_VIOLATION, "002", "여행 일정 요청 값이 올바르지 않습니다"),
    ALREADY_EXISTS_USER_TRAVEL_SCHEDULE(StatusCode.BAD_REQUEST, DomainCode.TRAVEL,
        CategoryCode.BUSINESS_RULE_VIOLATION, "003", "이미 해당 기간에 내 여행 일정이 존재합니다");

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
