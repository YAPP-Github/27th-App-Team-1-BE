package com.yapp.ndgl.application.common.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.yapp.ndgl.common.exception.CommonErrorCode;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<ErrorResponse<?>> handleGlobalException(final GlobalException e) {
		log.error("비즈니스 예외 발생: {}", e.getErrorMessage(), e);
		return ResponseEntity
			.status(e.getStatusCode().getCode())
			.body(ErrorResponse.error(e.getBaseErrorCode()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse<?>> handleValidationException(
		final MethodArgumentNotValidException e) {
		log.error("유효성 검증 실패", e);

		List<Map<String, String>> errors = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(error -> {
				Map<String, String> errorMap = new HashMap<>();
				errorMap.put("field", error.getField());
				errorMap.put("message", error.getDefaultMessage());
				return errorMap;
			})
			.toList();

		return ResponseEntity
			.status(HttpStatus.UNPROCESSABLE_ENTITY)
			.body(ErrorResponse.error(CommonErrorCode.VALIDATION_ERRORS_IN_REQUEST_DATA, errors));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse<?>> handleMethodNotSupportedException(
		final HttpRequestMethodNotSupportedException e) {
		log.error("지원하지 않는 HTTP 메서드: {}", e.getMethod(), e);
		return ResponseEntity
			.status(HttpStatus.METHOD_NOT_ALLOWED)
			.body(ErrorResponse.error(CommonErrorCode.METHOD_NOT_ALLOWED));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse<?>> handleHttpMessageNotReadableException(
		final HttpMessageNotReadableException e) {
		log.error("요청 본문을 읽을 수 없거나 형식이 올바르지 않음", e);
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.error(CommonErrorCode.INVALID_REQUEST_BODY));
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<ErrorResponse<?>> handleMissingRequestHeaderException(
		final MissingRequestHeaderException e) {
		log.error("필수 헤더 누락: {}", e.getHeaderName(), e);
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.error(CommonErrorCode.MISSING_REQUEST_HEADER));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse<?>> handleMethodArgumentTypeMismatchException(
		final MethodArgumentTypeMismatchException e) {
		log.error("파라미터 타입 불일치: {}", e.getName(), e);
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.error(CommonErrorCode.TYPE_MISS_MATCH_ERRORS_IN_REQUEST_PARAM_DATA));
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse<?>> handleMissingServletRequestParameterException(
		final MissingServletRequestParameterException e) {
		log.error("필수 요청 파라미터 누락: {}", e.getParameterName(), e);
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.error(CommonErrorCode.MISSING_REQUEST_PARAMETER));
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse<?>> handleNoResourceFoundException(
		final NoResourceFoundException e) {
		log.error("존재하지 않는 URI 요청: {}", e.getResourcePath(), e);
		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(ErrorResponse.error(CommonErrorCode.NOT_FOUND_URI));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse<?>> handleUnexpectedException(final Exception e) {
		log.error("예상치 못한 예외 발생", e);
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ErrorResponse.error(CommonErrorCode.INTERNAL_SERVER_ERROR));
	}
}
