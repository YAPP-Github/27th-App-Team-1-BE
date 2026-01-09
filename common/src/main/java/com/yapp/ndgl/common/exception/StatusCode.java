package com.yapp.ndgl.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusCode {
	/**
	 * 2xx - Success
	 */
	SUCCESS(200), // 요청이 성공적으로 처리됨
	CREATED(201), // 리소스가 성공적으로 생성됨

	/**
	 * 4xx - Client Error
	 */
	BAD_REQUEST(400), // 잘못된 요청 (파라미터 오류, 형식 오류 등)
	UNAUTHORIZED(401), // 인증 필요 (로그인 필요)
	FORBIDDEN(403), // 권한 없음 (인증은 되었으나 접근 권한 없음)
	NOT_FOUND(404), // 요청한 리소스를 찾을 수 없음
	METHOD_NOT_ALLOWED(405), // 허용되지 않는 HTTP 메서드
	CONFLICT(409), // 리소스 충돌 (중복된 데이터 등)
	UNPROCESSABLE_ENTITY(422), // 요청은 정상이나 유효성 검증 실패
	TOO_MANY_REQUESTS(429), // 요청 횟수 제한 초과 (Rate Limiting)

	/**
	 * 5xx - Server Error
	 */
	INTERNAL_SERVER_ERROR(500), // 서버 내부 오류
	GATEWAY_TIMEOUT(504); // 외부 서비스 응답 시간 초과 (외부 API, DB, 마이크로서비스 등)

	private final int code;
}
