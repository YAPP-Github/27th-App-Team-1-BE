package com.yapp.ndgl.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryCode {
	INPUT_VALIDATION("01"),        // 입력값 검증 실패 (400, 422)
	RESOURCE_NOT_FOUND("02"),      // 리소스 없음 (404)
	RESOURCE_CONFLICT("03"),       // 리소스 충돌 (중복, 상태 충돌 등) (409)
	BUSINESS_RULE_VIOLATION("04"), // 비즈니스 규칙 위반 (400, 422)
	AUTHENTICATION("05"),          // 인증 실패 (로그인 안됨, 토큰 문제) (401)
	AUTHORIZATION("06"),           // 인가 실패 (권한 없음) (403)
	EXTERNAL_SERVICE("07"),        // 외부 서비스 오류 (504)
	INTERNAL_SYSTEM("08");         // 내부 시스템 오류 (500)

	private final String code;
}
