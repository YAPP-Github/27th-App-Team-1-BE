package com.yapp.ndgl.application.domains.place.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yapp.ndgl.application.domains.place.controller.response.PlaceDetailResponse;
import com.yapp.ndgl.common.response.ErrorResponse;

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
			content = @Content(
				schema = @Schema(implementation = PlaceDetailResponse.class),
				examples = @ExampleObject(
					name = "SUCCESS",
					value = """
						{
						    "code": "2000",
						    "message": "요청에 성공하였습니다.",
						    "data": {
						        "place": {
						            "id": "ChIJPR5EUkCNGGARyhvN1EVWEc0",
						            "name": "규카츠 모토무라 신주쿠 본점",
						            "thumbnail": "https://lh3.googleusercontent.com/places/ANXAkqGbrXxA6_1tCvdNIB0BxCAB1ovsQH2KHOY5sBZEDZ8MI86hg5WIjXA0Ts_l3lKhJre-RpTvHVSMKusLWnwsDCW3HbqOAx6l3D0=s4800-w4800-h3200",
						            "nationalPhoneNumber": "050-1722-2861",
						            "internationalPhoneNumber": "+81 50-1722-2861",
						            "formattedAddress": "일본 〒160-0021 Tokyo, Shinjuku City, Kabukichō, 1-chōme−２５−３ WaMall, B2F 西武新宿駅前ビル",
						            "location": {
						                "latitude": 35.6946268,
						                "longitude": 139.7016497
						            },
						            "userRatingCount": 7306,
						            "rating": 4.9,
						            "regularOpeningHours": [
						                "월요일: AM 11:00 ~ PM 10:00",
						                "화요일: AM 11:00 ~ PM 10:00",
						                "수요일: AM 11:00 ~ PM 10:00",
						                "목요일: AM 11:00 ~ PM 10:00",
						                "금요일: AM 11:00 ~ PM 10:00",
						                "토요일: AM 11:00 ~ PM 10:00",
						                "일요일: AM 11:00 ~ PM 10:00"
						            ],
						            "googleMapsUri": "https://maps.google.com/?cid=14776686710302251978&g_mp=CiVnb29nbGUubWFwcy5wbGFjZXMudjEuUGxhY2VzLkdldFBsYWNlEAIYBCAA",
						            "websiteUri": "https://www.gyukatsu-motomura.com/shop/shinjukuhonten"
						        }
						    }
						}
						"""
				)
			)
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
							  "message": "유효하지 않은 Place ID 입니다",
							  "errors": []
							}
							"""
					),
					@ExampleObject(
						name = "INVALID_PHOTO_NAME",
						value = """
							{
							  "code": "GMAP_PLACE-07-005",
							  "message": "유효하지 않은 Photo Name 입니다",
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
				examples = {
					@ExampleObject(
						name = "INTERNAL_SERVER_ERROR",
						value = """
							{
							  "code": "COMM-08-001",
							  "message": "서버 내부 오류가 발생했습니다",
							  "errors": []
							}
							"""
					),
					@ExampleObject(
						name = "RESPONSE_PARSE_FAILED",
						value = """
							{
							  "code": "GMAP_PLACE-07-004",
							  "message": "Google Maps Places API 응답 파싱에 실패했습니다",
							  "errors": []
							}
							"""
					)
				}
			)
		)
	})
	@GetMapping("/detail")
	ResponseEntity<?> readPlaceDetail(
		@Parameter(description = "Google Places 장소 ID", example = "ChIJSc8jdZORQTURu6BMwxrKbGg", required = true)
		@RequestParam("placeId") final String placeId
	);
}
