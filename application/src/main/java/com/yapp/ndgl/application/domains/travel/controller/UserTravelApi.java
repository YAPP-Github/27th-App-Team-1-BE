package com.yapp.ndgl.application.domains.travel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yapp.ndgl.application.domains.auth.annotation.CurrentUuid;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserTravelRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UpcomingUserTravelResponse;
import com.yapp.ndgl.common.response.ErrorResponse;
import com.yapp.ndgl.common.response.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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
        summary = "다가오는 여행 조회",
        description = "사용자의 가장 가까운 예정 여행과 첫 일정 정보를 조회합니다. 없을 경우 null 반환.",
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
                          "userTravelId": 1,
                          "title": "여행 제목",
                          "country": "IN",
                          "city": "뭄바이",
                          "startDate": "2023-08-01",
                          "endDate": "2023-08-10",
                          "nights": 6,
                          "days": 7,
                          "upcomingUserTravelPlace": {
                            "id": 1,
                            "estimatedDuration": 60,
                            "place": {
                              "googlePlaceId": "ChIJSc8jdZORQTURu6BMwxrKbGg",
                              "thumbnail": "https://lh3.googleusercontent.com/place-photos/AEkURDym40I4XyqXUosRz8bTu9aPvDUklxkfM79KCa03C0SQTnDaTu_RXXiWQjCRZ3-yK4dTbzoySqMrucj1ubPQNUZ5yKseTRfmaME5C--5jLYB0rU-MLXqUabNEk3myTWywzIuEHcKz_I-H4Xtdg=s4800-w4800-h3600",
                              "latitude": 35.6762,
                              "longitude": 139.6503,
                              "name": "도쿄타워",
                              "regularOpeningHours": "09:00~23:00",
                              "googleMapsUri": "https://maps.google.com/?cid=14776686710302251978"
                            }
                          }
                        }
                        """
                )
            )
        )
    })
    ResponseEntity<UpcomingUserTravelResponse> getUpcomingUserTravel(
        @CurrentUuid String uuid
    );
}
