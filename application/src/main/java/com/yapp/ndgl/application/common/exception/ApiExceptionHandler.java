package com.yapp.ndgl.application.common.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
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

import com.yapp.ndgl.common.exception.BaseErrorCode;
import com.yapp.ndgl.common.exception.CategoryCode;
import com.yapp.ndgl.common.exception.CommonErrorCode;
import com.yapp.ndgl.common.exception.ErrorCausedBy;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.response.ErrorResponse;

import static net.logstash.logback.argument.StructuredArguments.kv;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(GlobalException.class)
	public ResponseEntity<ErrorResponse<?>> handleGlobalException(final GlobalException e) {
		logByErrorCode(e.getBaseErrorCode(), "비즈니스 예외 발생", e, null);
		return ResponseEntity
			.status(e.getStatusCode().getCode())
			.body(ErrorResponse.error(e.getBaseErrorCode()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse<?>> handleValidationException(
		final MethodArgumentNotValidException e) {
		BaseErrorCode errorCode = CommonErrorCode.VALIDATION_ERRORS_IN_REQUEST_DATA;

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

		logByErrorCode(errorCode, "유효성 검증 실패", e, errors);

		return ResponseEntity
			.status(HttpStatus.UNPROCESSABLE_ENTITY)
			.body(ErrorResponse.error(errorCode, errors));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse<?>> handleMethodNotSupportedException(
		final HttpRequestMethodNotSupportedException e) {
		BaseErrorCode errorCode = CommonErrorCode.METHOD_NOT_ALLOWED;
		logByErrorCode(errorCode, "지원하지 않는 HTTP 메서드", e, kv("method", e.getMethod()));
		return ResponseEntity
			.status(HttpStatus.METHOD_NOT_ALLOWED)
			.body(ErrorResponse.error(errorCode));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse<?>> handleHttpMessageNotReadableException(
		final HttpMessageNotReadableException e) {
		BaseErrorCode errorCode = CommonErrorCode.INVALID_REQUEST_BODY;
		logByErrorCode(errorCode, "요청 본문을 읽을 수 없거나 형식이 올바르지 않음", e, null);
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.error(errorCode));
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	public ResponseEntity<ErrorResponse<?>> handleMissingRequestHeaderException(
		final MissingRequestHeaderException e) {
		BaseErrorCode errorCode = CommonErrorCode.MISSING_REQUEST_HEADER;
		logByErrorCode(errorCode, "필수 헤더 누락", e, kv("headerName", e.getHeaderName()));
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.error(errorCode));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse<?>> handleMethodArgumentTypeMismatchException(
		final MethodArgumentTypeMismatchException e) {
		BaseErrorCode errorCode = CommonErrorCode.TYPE_MISS_MATCH_ERRORS_IN_REQUEST_PARAM_DATA;
		logByErrorCode(errorCode, "파라미터 타입 불일치", e, kv("parameterName", e.getName()));
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.error(errorCode));
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse<?>> handleMissingServletRequestParameterException(
		final MissingServletRequestParameterException e) {
		BaseErrorCode errorCode = CommonErrorCode.MISSING_REQUEST_PARAMETER;
		logByErrorCode(errorCode, "필수 요청 파라미터 누락", e, kv("parameterName", e.getParameterName()));
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ErrorResponse.error(errorCode));
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse<?>> handleNoResourceFoundException(
		final NoResourceFoundException e) {
		BaseErrorCode errorCode = CommonErrorCode.NOT_FOUND_URI;
		logByErrorCode(errorCode, "존재하지 않는 URI 요청", e, kv("resourcePath", e.getResourcePath()));
		return ResponseEntity
			.status(HttpStatus.NOT_FOUND)
			.body(ErrorResponse.error(errorCode));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse<?>> handleConstraintViolationException(
		final ConstraintViolationException e) {
		BaseErrorCode errorCode = CommonErrorCode.VALIDATION_ERRORS_IN_REQUEST_DATA;

		List<Map<String, String>> errors = e.getConstraintViolations()
			.stream()
			.map(violation -> {
				Map<String, String> errorMap = new HashMap<>();
				String field = "";
				for (jakarta.validation.Path.Node node : violation.getPropertyPath()) {
					if (node.getName() != null) {
						field = node.getName();
					}
				}
				errorMap.put("field", field);
				errorMap.put("message", violation.getMessage());
				return errorMap;
			})
			.toList();

		logByErrorCode(errorCode, "유효성 검증 실패", e, errors);

		return ResponseEntity
			.status(HttpStatus.UNPROCESSABLE_ENTITY)
			.body(ErrorResponse.error(errorCode, errors));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse<?>> handleDataIntegrityViolationException(
		final DataIntegrityViolationException e) {
		String rootCauseMessage = e.getMostSpecificCause().getMessage();
		if (rootCauseMessage != null && rootCauseMessage.toLowerCase().contains("duplicate")) {
			BaseErrorCode errorCode = CommonErrorCode.DATA_INTEGRITY_VIOLATION;
			logByErrorCode(errorCode, "유니크 제약 조건 위반 (race condition)", e, null);
			return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(ErrorResponse.error(errorCode));
		}
		BaseErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
		logByErrorCode(errorCode, "예상치 못한 데이터 무결성 위반", e, null);
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ErrorResponse.error(errorCode));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse<?>> handleUnexpectedException(final Exception e) {
		BaseErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
		logByErrorCode(errorCode, "예상치 못한 예외 발생", e, null);
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ErrorResponse.error(errorCode));
	}

	private void logByErrorCode(BaseErrorCode errorCode, String message, Exception exception, Object extra) {
		ErrorCausedBy causedBy = errorCode.errorCausedBy();
		String fullCode = causedBy.getErrorCode();
		int status = errorCode.getStatusCode().getCode();
		CategoryCode category = causedBy.categoryCode();

		if (isErrorLevel(category)) {
			if (extra == null) {
				log.error(message, kv("errorCode", fullCode), kv("status", status), kv("category", category.name()), exception);
			} else if (extra instanceof Map<?, ?> map) {
				log.error(message, kv("errorCode", fullCode), kv("status", status), kv("category", category.name()), kv("errors", map), exception);
			} else if (extra instanceof List<?> list) {
				log.error(message, kv("errorCode", fullCode), kv("status", status), kv("category", category.name()), kv("errors", list), exception);
			} else {
				log.error(message, kv("errorCode", fullCode), kv("status", status), kv("category", category.name()), extra, exception);
			}
			return;
		}

		if (extra == null) {
			log.warn(message, kv("errorCode", fullCode), kv("status", status), kv("category", category.name()));
		} else if (extra instanceof Map<?, ?> map) {
			log.warn(message, kv("errorCode", fullCode), kv("status", status), kv("category", category.name()), kv("errors", map));
		} else if (extra instanceof List<?> list) {
			log.warn(message, kv("errorCode", fullCode), kv("status", status), kv("category", category.name()), kv("errors", list));
		} else {
			log.warn(message, kv("errorCode", fullCode), kv("status", status), kv("category", category.name()), extra);
		}
	}

	private boolean isErrorLevel(CategoryCode category) {
		return category == CategoryCode.EXTERNAL_SERVICE || category == CategoryCode.INTERNAL_SYSTEM;
	}
}
