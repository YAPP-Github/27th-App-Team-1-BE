package com.yapp.ndgl.common.exception;

import java.util.Objects;

public record ErrorCausedBy(DomainCode domainCode, CategoryCode categoryCode, String detailCode) {

	public ErrorCausedBy {
		Objects.requireNonNull(domainCode, "domain code는 null이 될 수 없습니다.");
		Objects.requireNonNull(categoryCode, "category code는 null이 될 수 없습니다.");
		Objects.requireNonNull(detailCode, "detail code는 null이 될 수 없습니다.");
	}

	public static ErrorCausedBy of(final DomainCode domainCode, final CategoryCode categoryCode, final String detailCode) {
		return new ErrorCausedBy(domainCode, categoryCode, detailCode);
	}

	public String getErrorCode() {
		return domainCode.name() + "-" + categoryCode.getCode() + "-" + detailCode;
	}
	
}
