package com.yapp.ndgl.application.survey.controller;

import com.yapp.ndgl.application.auth.annotation.CurrentUuid;
import com.yapp.ndgl.application.survey.controller.dto.OnboardingSurveyCreateRequest;
import com.yapp.ndgl.common.response.ErrorResponse;
import com.yapp.ndgl.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/surveys")
public interface SurveyApi {

    @Operation(
        summary = "온보딩 설문조사 생성",
        description = "회원가입 시 사용자의 여행 스타일 온보딩 설문조사를 생성합니다.",
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
            responseCode = "409",
            description = "이미 온보딩 설문조사가 존재함",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "ALREADY_EXISTS_ONBOARDING_SURVEY",
                    value = """
                        {
                          "code": "SURVEY-03-001",
                          "message": "이미 온보딩 설문조사가 존재합니다",
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
                              "field": "interestRegions",
                              "message": "관심 지역은 최소 1개 이상 선택해야 합니다."
                            },
                            {
                              "field": "scheduleType",
                              "message": "일정 스타일은 필수입니다."
                            },
                            {
                              "field": "contentPreferences",
                              "message": "여행 콘텐츠 취향은 최소 1개 이상 선택해야 합니다."
                            },
                            {
                              "field": "travelCompanions",
                              "message": "동행은 최소 1개 이상 선택해야 합니다."
                            }
                          ]
                        }
                        """
                )
            )
        )
    })
    @PostMapping("/onboarding")
    ResponseEntity<SuccessResponse> createOnboardingSurvey(
        @CurrentUuid String uuid,
        @Valid @RequestBody OnboardingSurveyCreateRequest request
    );
}
