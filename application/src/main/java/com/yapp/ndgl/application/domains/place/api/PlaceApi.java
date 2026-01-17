package com.yapp.ndgl.application.domains.place.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yapp.ndgl.common.response.ErrorResponse;
import com.yapp.ndgl.common.response.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RequestMapping("/api/v1/places")
public interface PlaceApi {

	@Operation(
		summary = "장소 상세 조회",
		description = "placeId로 Google Places 상세 정보를 조회한다."
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "성공",
			content = @Content(schema = @Schema(implementation = SuccessResponse.class))
		),
		@ApiResponse(
			responseCode = "400",
			description = "잘못된 요청",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = {
					@ExampleObject(
						name = "MISSING_REQUEST_PARAMETER",
						value = """
							{
							  "code": "COMM-01-006",
							  "message": "필수 요청 파라미터가 존재하지 않습니다",
							  "errors": []
							}
							"""
					),
					@ExampleObject(
						name = "INVALID_PLACE_ID",
						value = """
							{
							  "code": "GMAP_PLACE-07-003",
							  "message": "유효하지 않은 Place ID입니다",
							  "errors": []
							}
							"""
					)
				}
			)
		),
		@ApiResponse(
			responseCode = "504",
			description = "외부 API 호출 실패 또는 타임아웃",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = {
					@ExampleObject(
						name = "API_CALL_FAILED",
						value = """
							{
							  "code": "GMAP_PLACE-07-001",
							  "message": "Google Maps Places API 호출에 실패했습니다",
							  "errors": []
							}
							"""
					),
					@ExampleObject(
						name = "API_TIMEOUT",
						value = """
							{
							  "code": "GMAP_PLACE-07-002",
							  "message": "Google Maps Places API 응답 시간이 초과되었습니다",
							  "errors": []
							}
							"""
					)
				}
			)
		),
		@ApiResponse(
			responseCode = "500",
			description = "서버 내부 오류",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "INTERNAL_SERVER_ERROR",
					value = """
						{
						  "code": "COMM-08-001",
						  "message": "서버 내부 오류가 발생했습니다",
						  "errors": []
						}
						"""
				)
			)
		)
	})
	@GetMapping("/detail")
	ResponseEntity<?> readPlaceDetail(
		@Parameter(description = "Google Places 장소 ID", example = "ChIJSc8jdZORQTURu6BMwxrKbGg", required = true)
		@RequestParam("placeId") final String placeId
	);
}
