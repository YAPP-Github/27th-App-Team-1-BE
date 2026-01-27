package com.yapp.ndgl.application.domains.travel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yapp.ndgl.application.domains.auth.annotation.CurrentUuid;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserTravelRequest;
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
                            "id": 1
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
}
