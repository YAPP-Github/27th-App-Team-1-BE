package com.yapp.ndgl.application.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.yapp.ndgl.application.controller.TestController;

@WebMvcTest(TestController.class)
class ApiExceptionHandlerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("성공 응답 테스트")
	void testSuccess() throws Exception {
		mockMvc.perform(get("/api/test/success"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("2000"))
			.andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
			.andExpect(jsonPath("$.data.message").value("테스트 성공"));
	}

	@Test
	@DisplayName("데이터 없는 성공 응답 테스트")
	void testNoContent() throws Exception {
		mockMvc.perform(get("/api/test/no-content"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("2000"))
			.andExpect(jsonPath("$.message").value("요청에 성공하였습니다."))
			.andExpect(jsonPath("$.data").isEmpty());
	}

	@Test
	@DisplayName("GlobalException 테스트 - TOO_MANY_REQUESTED")
	void testGlobalException() throws Exception {
		mockMvc.perform(get("/api/test/global-exception"))
			.andDo(print())
			.andExpect(status().isTooManyRequests())
			.andExpect(jsonPath("$.code").value("COMM-04-001"))
			.andExpect(jsonPath("$.message").value("너무 많은 요청이 감지되었습니다. 잠시 후 다시 시도하세요."))
			.andExpect(jsonPath("$.errors").isArray())
			.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	@DisplayName("Validation 실패 테스트 - 빈 body")
	void testValidationFailure() throws Exception {
		String invalidJson = "{}";

		mockMvc.perform(post("/api/test/validation")
				.contentType(MediaType.APPLICATION_JSON)
				.content(invalidJson))
			.andDo(print())
			.andExpect(status().isUnprocessableEntity())
			.andExpect(jsonPath("$.code").value("COMM-01-005"))
			.andExpect(jsonPath("$.message").value("유효성 검증에 실패하였습니다"))
			.andExpect(jsonPath("$.errors").isArray())
			.andExpect(jsonPath("$.errors[0].field").exists())
			.andExpect(jsonPath("$.errors[0].message").exists());
	}

	@Test
	@DisplayName("Validation 실패 테스트 - 잘못된 이메일 형식")
	void testValidationFailureInvalidEmail() throws Exception {
		String invalidJson = """
			{
				"name": "테스트",
				"email": "invalid-email"
			}
			""";

		mockMvc.perform(post("/api/test/validation")
				.contentType(MediaType.APPLICATION_JSON)
				.content(invalidJson))
			.andDo(print())
			.andExpect(status().isUnprocessableEntity())
			.andExpect(jsonPath("$.code").value("COMM-01-005"))
			.andExpect(jsonPath("$.message").value("유효성 검증에 실패하였습니다"))
			.andExpect(jsonPath("$.errors").isArray());
	}

	@Test
	@DisplayName("필수 헤더 누락 테스트")
	void testMissingRequestHeader() throws Exception {
		mockMvc.perform(get("/api/test/required-header"))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("COMM-01-001"))
			.andExpect(jsonPath("$.message").value("필수 헤더가 존재하지 않습니다"))
			.andExpect(jsonPath("$.errors").isArray())
			.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	@DisplayName("필수 헤더 있는 경우 성공 테스트")
	void testWithRequiredHeader() throws Exception {
		mockMvc.perform(get("/api/test/required-header")
				.header("X-Required-Header", "test-value"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("2000"))
			.andExpect(jsonPath("$.data.header").value("test-value"));
	}

	@Test
	@DisplayName("파라미터 타입 불일치 테스트")
	void testTypeMismatch() throws Exception {
		mockMvc.perform(get("/api/test/type-mismatch")
				.param("age", "invalid-number"))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("COMM-01-003"))
			.andExpect(jsonPath("$.message").value("허용되지 않는 쿼리입니다."))
			.andExpect(jsonPath("$.errors").isArray())
			.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	@DisplayName("필수 파라미터 누락 테스트")
	void testMissingParameter() throws Exception {
		mockMvc.perform(get("/api/test/required-param"))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("COMM-01-006"))
			.andExpect(jsonPath("$.message").value("필수 요청 파라미터가 존재하지 않습니다"))
			.andExpect(jsonPath("$.errors").isArray())
			.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	@DisplayName("필수 파라미터 있는 경우 성공 테스트")
	void testWithRequiredParameter() throws Exception {
		mockMvc.perform(get("/api/test/required-param")
				.param("name", "테스트"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value("2000"))
			.andExpect(jsonPath("$.data.name").value("테스트"));
	}

	@Test
	@DisplayName("잘못된 JSON 형식 테스트")
	void testInvalidJson() throws Exception {
		String invalidJson = "{invalid json}";

		mockMvc.perform(post("/api/test/invalid-json")
				.contentType(MediaType.APPLICATION_JSON)
				.content(invalidJson))
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("COMM-01-002"))
			.andExpect(jsonPath("$.message").value("요청 본문을 읽을 수 없거나 형식이 올바르지 않습니다."))
			.andExpect(jsonPath("$.errors").isArray())
			.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	@DisplayName("지원하지 않는 HTTP 메서드 테스트")
	void testMethodNotAllowed() throws Exception {
		mockMvc.perform(post("/api/test/only-get"))
			.andDo(print())
			.andExpect(status().isMethodNotAllowed())
			.andExpect(jsonPath("$.code").value("COMM-01-004"))
			.andExpect(jsonPath("$.message").value("요청하신 HTTP 메서드는 이 리소스에서 지원되지 않습니다."))
			.andExpect(jsonPath("$.errors").isArray())
			.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	@DisplayName("예상치 못한 예외 테스트")
	void testUnexpectedException() throws Exception {
		mockMvc.perform(get("/api/test/unexpected-error"))
			.andDo(print())
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.code").value("COMM-08-001"))
			.andExpect(jsonPath("$.message").value("서버 내부 오류가 발생했습니다"))
			.andExpect(jsonPath("$.errors").isArray())
			.andExpect(jsonPath("$.errors").isEmpty());
	}

	@Test
	@DisplayName("존재하지 않는 URI 요청 테스트")
	void testNotFoundUri() throws Exception {
		mockMvc.perform(get("/api/test/not-exists-uri"))
			.andDo(print())
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code").value("COMM-02-001"))
			.andExpect(jsonPath("$.message").value("요청하신 URI를 찾을 수 없습니다"))
			.andExpect(jsonPath("$.errors").isArray())
			.andExpect(jsonPath("$.errors").isEmpty());
	}
}
