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
        CategoryCode.RESOURCE_NOT_FOUND, "001", "여행 템플릿을 찾을 수 없습니다");

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
