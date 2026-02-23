package com.yapp.ndgl.application.domains.travel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

import com.yapp.ndgl.application.domains.auth.annotation.CurrentUuid;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserTravelRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.ReplaceUserTravelItineraryRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpdateUserTravelPlaceStartTimesRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpdateUserTravelRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpcomingUserTravelListResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpcomingUserTravelResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.UserTravelContentCardResponse;
import com.yapp.ndgl.application.domains.travel.controller.dto.UserTravelItineraryResponse;
import com.yapp.ndgl.common.response.ErrorResponse;
import com.yapp.ndgl.common.response.SliceResponse;
import com.yapp.ndgl.common.response.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@Tag(name = "User Travel", description = "사용자 여행 관련 API")
@RequestMapping("/api/v1/travels")
public interface UserTravelApi {

	@Operation(
		summary = "템플릿으로 내 여행 생성",
		description = "여행 템플릿을 기반으로 사용자의 여행을 생성합니다. 사용자가 선택한 날짜 범위 내의 일정만 복사됩니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "성공",
			content = @Content(
				schema = @Schema(implementation = SuccessResponse.class),
				examples = @ExampleObject(
					name = "SUCCESS",
					value = """
						{
						  "code": "2000",
						  "message": "요청에 성공하였습니다.",
						  "data": {
						    "userTravelId": 1
						  }
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "여행 종료일이 시작일보다 앞설 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "INVALID_DATE_ORDER",
					value = """
						{
						  "code": "TRAVEL-04-001",
						  "message": "여행 종료일이 시작일보다 앞설 수 없습니다",
						  "errors": []
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "기존 내 여행 일정과 기간이 겹침",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "ALREADY_EXISTS_USER_TRAVEL_SCHEDULE",
					value = """
						{
						  "code": "TRAVEL-04-003",
						  "message": "이미 해당 기간에 내 여행 일정이 존재합니다",
						  "errors": []
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "여행 템플릿을 찾을 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "NOT_FOUND_TRAVEL_TEMPLATE",
					value = """
						{
						  "code": "TRAVEL-02-001",
						  "message": "여행 템플릿을 찾을 수 없습니다",
						  "errors": []
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "422",
			description = "유효성 검증 실패",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "VALIDATION_ERROR",
					value = """
						{
						  "code": "COMM-01-005",
						  "message": "유효성 검증에 실패하였습니다",
						  "errors": [
						    {
						      "field": "templateId",
						      "message": "여행 템플릿 ID는 필수입니다."
						    },
						    {
						      "field": "startDate",
						      "message": "여행 시작일은 필수입니다."
						    },
						    {
						      "field": "endDate",
						      "message": "여행 종료일은 필수입니다."
						    }
						  ]
						}
						"""
				)
			)
		)
	})
	@PostMapping
	ResponseEntity<?> createUserTravel(
		@CurrentUuid String uuid,
		@Valid @RequestBody CreateUserTravelRequest request
	);

	@Operation(
		summary = "내 여행 수정",
		description = "사용자 본인의 여행 제목과 날짜를 수정합니다. nights/days는 서버에서 재계산됩니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "성공",
			content = @Content(
				schema = @Schema(implementation = SuccessResponse.class),
				examples = @ExampleObject(
					name = "SUCCESS",
					value = """
						{
						  "code": "2000",
						  "message": "요청에 성공하였습니다.",
						  "data": {}
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "여행 종료일이 시작일보다 앞설 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "INVALID_DATE_ORDER",
					value = """
						{
						  "code": "TRAVEL-04-001",
						  "message": "여행 종료일이 시작일보다 앞설 수 없습니다",
						  "errors": []
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "기존 내 여행 일정과 기간이 겹침",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "ALREADY_EXISTS_USER_TRAVEL_SCHEDULE",
					value = """
						{
						  "code": "TRAVEL-04-003",
						  "message": "이미 해당 기간에 내 여행 일정이 존재합니다",
						  "errors": []
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "내 여행을 찾을 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "NOT_FOUND_USER_TRAVEL",
					value = """
						{
						  "code": "TRAVEL-02-002",
						  "message": "내 여행 정보를 찾을 수 없습니다",
						  "errors": []
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "422",
			description = "유효성 검증 실패",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "VALIDATION_ERROR",
					value = """
						{
						  "code": "COMM-01-005",
						  "message": "유효성 검증에 실패하였습니다",
						  "errors": [
						    {
						      "field": "title",
						      "message": "여행 제목은 필수입니다."
						    }
						  ]
						}
						"""
				)
			)
		)
	})
	@PatchMapping("/{id}")
	ResponseEntity<SuccessResponse> updateUserTravel(
		@CurrentUuid String uuid,
		@Parameter(description = "사용자 여행 ID", example = "1", required = true)
		@PathVariable("id") final Long id,
		@Valid @RequestBody UpdateUserTravelRequest request
	);

	@Operation(
		summary = "내 여행 일정 전체 수정",
		description = "사용자 본인 여행의 전체 일정을 요청 목록으로 교체합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "성공",
			content = @Content(
				schema = @Schema(implementation = SuccessResponse.class),
				examples = @ExampleObject(
					name = "SUCCESS",
					value = """
						{
						  "code": "2000",
						  "message": "요청에 성공하였습니다.",
						  "data": {}
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "400",
			description = "일정 요청 값이 잘못됨",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "INVALID_ITINERARY_REQUEST",
					value = """
						{
						  "code": "TRAVEL-04-002",
						  "message": "여행 일정 요청 값이 올바르지 않습니다",
						  "errors": []
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "내 여행 또는 장소를 찾을 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = {
					@ExampleObject(
						name = "NOT_FOUND_USER_TRAVEL",
						value = """
							{
							  "code": "TRAVEL-02-002",
							  "message": "내 여행 정보를 찾을 수 없습니다",
							  "errors": []
							}
							"""
					),
					@ExampleObject(
						name = "NOT_FOUND_PLACE",
						value = """
							{
							  "code": "PLACE-02-001",
							  "message": "장소를 찾을 수 없습니다",
							  "errors": []
							}
							"""
					)
				}
			)
		),
		@ApiResponse(
			responseCode = "422",
			description = "유효성 검증 실패",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "VALIDATION_ERROR",
					value = """
						{
						  "code": "COMM-01-005",
						  "message": "유효성 검증에 실패하였습니다",
						  "errors": [
						    {
						      "field": "itineraries",
						      "message": "일정 목록은 최소 1개 이상이어야 합니다."
						    }
						  ]
						}
						"""
				)
			)
		)
	})
	@PutMapping("/{id}/itinerary")
	ResponseEntity<SuccessResponse> replaceUserTravelItinerary(
		@CurrentUuid String uuid,
		@Parameter(description = "사용자 여행 ID", example = "1", required = true)
		@PathVariable("id") final Long id,
		@Valid @RequestBody ReplaceUserTravelItineraryRequest request
	);

	@Operation(
		summary = "내 여행 startTime 일괄 수정",
		description = "사용자 본인 여행의 장소 startTime을 id/startTime 쌍으로 일괄 수정합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "성공",
			content = @Content(
				schema = @Schema(implementation = SuccessResponse.class),
				examples = @ExampleObject(
					name = "SUCCESS",
					value = """
						{
						  "code": "2000",
						  "message": "요청에 성공하였습니다.",
						  "data": {}
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "내 여행을 찾을 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "NOT_FOUND_USER_TRAVEL",
					value = """
						{
						  "code": "TRAVEL-02-002",
						  "message": "내 여행 정보를 찾을 수 없습니다",
						  "errors": []
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "422",
			description = "유효성 검증 실패",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "VALIDATION_ERROR",
					value = """
						{
						  "code": "COMM-01-005",
						  "message": "유효성 검증에 실패하였습니다",
						  "errors": [
						    {
						      "field": "updates",
						      "message": "업데이트 목록은 최소 1개 이상이어야 합니다."
						    }
						  ]
						}
						"""
				)
			)
		)
	})
	@PatchMapping("/{id}/start-time/bulk")
	ResponseEntity<?> bulkUpdateUserTravelPlaceStartTimes(
		@CurrentUuid String uuid,
		@Parameter(description = "사용자 여행 ID", example = "1", required = true)
		@PathVariable("id") final Long id,
		@Valid @RequestBody UpdateUserTravelPlaceStartTimesRequest request
	);

	@Operation(
		summary = "다가오는 여행 조회",
		description = "사용자의 가장 가까운 예정 여행과 첫 일정 정보를 조회합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "성공",
			content = @Content(
				schema = @Schema(implementation = UpcomingUserTravelResponse.class),
				examples = @ExampleObject(
					name = "SUCCESS",
					value = """
						{
						  "code": "2000",
						  "message": "요청에 성공하였습니다.",
						  "data": {
						    "userTravelId": 3,
						    "title": "세계에서 가장 추운 도시",
						    "country": "RU",
						    "countryName": "러시아",
						    "city": "야쿠츠크",
						    "startDate": "2026-03-01",
						    "endDate": "2026-03-05",
						    "nights": 4,
						    "days": 5,
						    "thumbnail": "https://i.ytimg.com/vi/umCW_TFNTPI/maxresdefault.jpg",
						    "upcomingUserTravelPlace": {
						      "id": 12,
						      "estimatedDuration": 60,
						      "place": {
						        "googlePlaceId": "ChIJfavmvuFL9lsRsFs_r_9Zoq8",
						        "thumbnail": "https://lh3.googleusercontent.com/place-photos/AL8-SNGUyYW3K-Ar0KVPtDG5HMa8BwygccVS3n30vZ2gwcEjNcHQ85StpWn65JCJw8aD9u6xCjh6N5cf1gQj17LsSg4Gs7EwyXYsy7n5djO3VqcnAsttWoK5wnOPof7oOpRJE7pIRETVh_chQH2tGw=s4800-w4032-h3024",
						        "latitude": 62.086598099999996,
						        "longitude": 129.7500322,
						        "name": "야쿠츠크 공항",
						        "regularOpeningHours": "AM 12:00 ~ PM 2:00",
						        "googleMapsUri": "https://maps.google.com/?cid=12655776857556212656&g_mp=CiVnb29nbGUubWFwcy5wbGFjZXMudjEuUGxhY2VzLkdldFBsYWNlEAIYBCAA",
						        "category": "AIRPORT"
						      }
						    }
						  }
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "204",
			description = "다가오는 여행 없음"
		)
	})
	ResponseEntity<SuccessResponse<UpcomingUserTravelResponse>> getUpcomingUserTravel(
		@CurrentUuid String uuid
	);

	@Operation(
		summary = "내 여행 예정 목록 조회",
		description = "여행 시작일이 오늘보다 미래인 내 여행 목록을 시작일 오름차순으로 조회합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "성공",
			content = @Content(
				schema = @Schema(implementation = UpcomingUserTravelListResponse.class),
				examples = @ExampleObject(
					name = "SUCCESS",
					value = """
						{
						  "code": "2000",
						  "message": "요청에 성공하였습니다.",
						  "data": {
						    "content": [
						      {
						        "id": 1,
						        "title": "도쿄 3박 4일",
						        "country": "JP",
						        "city": "도쿄",
						        "startDate": "2026-03-01",
						        "endDate": "2026-03-04",
						        "nights": 3,
						        "days": 4,
						        "templateId": 10,
						        "thumbnail": "https://example.com/thumbnail.jpg",
						        "profileImage": "https://example.com/profile.jpg"
						      }
						    ],
						    "hasNext": true
						  }
						}
						"""
				)
			)
		)
	})
	ResponseEntity<SuccessResponse<SliceResponse<UpcomingUserTravelListResponse>>> getUpcomingUserTravels(
		@CurrentUuid String uuid,
		@Parameter(description = "페이지 번호 (0부터 시작)", example = "0", required = false)
		@Min(value = 0, message = "page는 0 이상 입니다.")
		@RequestParam(value = "page", defaultValue = "0") final int page,
		@Parameter(description = "페이지 사이즈", example = "20", required = false)
		@RequestParam(value = "size", defaultValue = "20")
		@Min(value = 1, message = "size는 1 이상 입니다.") final int size
	);

	@Operation(
		summary = "내 여행 상단 정보 조회",
		description = "사용자 본인의 여행 상단 정보(content-card)를 조회합니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "성공",
			content = @Content(
				schema = @Schema(implementation = UserTravelContentCardResponse.class),
				examples = @ExampleObject(
					name = "SUCCESS",
					value = """
							{
							  "code": "2000",
							  "message": "요청에 성공하였습니다.",
							  "data": {
							    "userTravelId": 1,
							    "travelId": 1,
							    "templateId": 3,
							    "title": "도쿄 3박 4일",
							    "country": "JP",
							    "city": "도쿄",
							    "startDate": "2026-03-01",
							    "endDate": "2026-03-04",
							    "budgetPerPerson": 1200000,
							    "nights": 3,
							    "days": 4,
							    "youtube": {
							      "title": "도쿄 3박 4일 완벽 여행 가이드",
							      "name": "빠니보틀",
							      "profileImage": "https://example.com/thumbnail/panibottle.jpg",
							      "thumbnail": "https://example.com/thumbnail/tokyo.jpg",
							      "link": "https://www.youtube.com/watch?v=tokyo-travel",
							      "summary": "도쿄 3박 4일 여행의 모든 것."
							    },
							    "program": {
							      "title": "도쿄 3박 4일 완벽 여행 가이드",
							      "name": "빠니보틀",
							      "profileImage": "https://example.com/thumbnail/panibottle.jpg",
							      "thumbnail": "https://example.com/thumbnail/tokyo.jpg",
							      "link": "https://www.youtube.com/watch?v=tokyo-travel",
							      "summary": "도쿄 3박 4일 여행의 모든 것."
							    }
							  }
							}
							"""
				)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "내 여행을 찾을 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "NOT_FOUND_USER_TRAVEL",
					value = """
						{
						  "code": "TRAVEL-02-002",
						  "message": "내 여행 정보를 찾을 수 없습니다",
						  "errors": []
						}
						"""
				)
			)
		)
	})
	ResponseEntity<SuccessResponse<UserTravelContentCardResponse>> readUserTravelContentCard(
		@CurrentUuid String uuid,
		@Parameter(description = "사용자 여행 ID", example = "1", required = true)
		@PathVariable("id") final Long id
	);

	@Operation(
		summary = "내 여행 일정 조회",
		description = "사용자 본인의 여행 일정(itinerary)을 조회합니다. day 파라미터로 특정 일차를 조회할 수 있습니다.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "성공",
			content = @Content(
				schema = @Schema(implementation = UserTravelItineraryResponse.class),
				examples = @ExampleObject(
					name = "SUCCESS",
					value = """
						{
						  "code": "2000",
						  "message": "요청에 성공하였습니다.",
						  "data": {
						    "itineraries": [
						      {
						        "id": 101,
						        "day": 1,
						        "sequence": 1,
						        "distanceKm": 1.2,
						        "transportation": [
						          {
						            "mode": "WALKING",
						            "timeMin": 15
						          }
						        ],
						        "travelerTip": "전망대는 오전 일찍 가면 대기 시간이 짧아요.",
						        "travelerTips": [
						          "전망대는 오전 일찍 가면 대기 시간이 짧아요.",
						          "야경 감상을 원하면 일몰 30분 전에 도착하세요."
						        ],
						        "planB": [],
						        "startTime": "08:30:00",
						        "estimatedDuration": 60,
						        "budget": 50000,
						        "place": {
						          "googlePlaceId": "ChIJSc8jdZORQTURu6BMwxrKbGg",
						          "thumbnail": "https://example.com/thumbnail/tokyo.jpg",
						          "latitude": 35.6585805,
						          "longitude": 139.7454329,
						          "name": "Tokyo Tower",
						          "regularOpeningHours": "09:00~23:00",
						          "googleMapsUri": "https://maps.google.com/?cid=10281119591005088802",
						          "category": "ATTRACTION"
						        }
						      }
						    ]
						  }
						}
						"""
				)
			)
		),
		@ApiResponse(
			responseCode = "404",
			description = "내 여행을 찾을 수 없음",
			content = @Content(
				schema = @Schema(implementation = ErrorResponse.class),
				examples = @ExampleObject(
					name = "NOT_FOUND_USER_TRAVEL",
					value = """
						{
						  "code": "TRAVEL-02-002",
						  "message": "내 여행 정보를 찾을 수 없습니다",
						  "errors": []
						}
						"""
				)
			)
		)
	})
	ResponseEntity<SuccessResponse<UserTravelItineraryResponse>> readUserTravelItinerary(
		@CurrentUuid String uuid,
		@Parameter(description = "사용자 여행 ID", example = "1", required = true)
		@PathVariable("id") final Long id,
		@Parameter(description = "조회할 일차", example = "1", required = true)
		@RequestParam(value = "day")
		@Min(value = 1, message = "day는 항상 1 이상 입니다.") final int day
	);
}
