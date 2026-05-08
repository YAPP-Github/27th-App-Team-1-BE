package com.yapp.ndgl.application.domains.travel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.yapp.ndgl.application.domains.auth.annotation.CurrentUuid;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserSuggestedTemplateRequest;
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

@Tag(name = "User Suggested Template", description = "사용자 제안 여행 템플릿 API")
public interface UserSuggestedTemplateApi {

    @Operation(
        summary = "여행 템플릿 제안 등록",
        description = """
            사용자가 YouTube 영상 링크와 추천 이유를 입력하여 여행 템플릿을 제안합니다.
            등록 시 상태는 PENDING으로 설정되며, 영상 URL 형식 유효성만 검증합니다.
            등록에 성공하면 요청자는 자동으로 해당 템플릿의 구독자로 등록됩니다.
            """,
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        content = @Content(
            examples = @ExampleObject(
                name = "요청 예시",
                value = """
                    {
                      "video_link": "https://youtu.be/abc12345678",
                      "recommend_reason": "산정호수 일출이 정말 아름다워요. 새벽 5시에 가면 혼자 볼 수 있어요.",
                      "category": "UNCATEGORIZED",
                      "region": "UNDEFINED"
                    }
                    """
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "등록 성공. 요청자가 구독자로 자동 등록됩니다.",
            content = @Content(
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
            description = "잘못된 YouTube 영상 URL",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "INVALID_VIDEO_URL",
                    value = """
                        {
                          "code": "YOUTUBE-01-001",
                          "message": "유효하지 않은 YouTube 영상 URL입니다",
                          "errors": []
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증 실패",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "UNAUTHORIZED",
                    value = """
                        {
                          "code": "COMM-05-001",
                          "message": "인증이 필요합니다. JWT 토큰을 확인해주세요.",
                          "errors": []
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = """
                중복 요청 (에러 코드에 따라 다음 처리를 분기하세요)
                - TRAVEL-03-006: TravelTemplate에 이미 게시된 영상. 추가 처리 불필요
                - TRAVEL-03-002: 이미 승인 처리된 영상. 추가 처리 불필요
                - TRAVEL-03-003: 다른 사용자가 PENDING 요청 중. 구독 API(POST /api/v1/suggested-templates/{id}/subscribe)를 통해 게시 알림 신청 가능
                - TRAVEL-03-004: 본인이 이미 PENDING 요청 중. 추가 처리 불필요
                """,
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "ALREADY_REGISTERED_TRAVEL_TEMPLATE",
                        summary = "TravelTemplate에 이미 게시된 영상",
                        value = """
                            {
                              "code": "TRAVEL-03-006",
                              "message": "이미 등록된 영상입니다",
                              "errors": []
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "ALREADY_EXISTS_TRAVEL_TEMPLATE",
                        summary = "이미 승인 처리된 영상",
                        value = """
                            {
                              "code": "TRAVEL-03-002",
                              "message": "이미 저장된 영상입니다",
                              "errors": []
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "ALREADY_EXISTS_SUGGESTED_TEMPLATE",
                        summary = "다른 사용자가 PENDING 요청 중",
                        value = """
                            {
                              "code": "TRAVEL-03-003",
                              "message": "다른 사용자가 이미 요청한 영상입니다.",
                              "errors": []
                            }
                            """
                    ),
                    @ExampleObject(
                        name = "ALREADY_REQUESTED_SUGGESTED_TEMPLATE",
                        summary = "본인이 이미 PENDING 요청 중",
                        value = """
                            {
                              "code": "TRAVEL-03-004",
                              "message": "이미 요청한 영상입니다",
                              "errors": []
                            }
                            """
                    )
                }
            )
        )
    })
    ResponseEntity<SuccessResponse<?>> createUserSuggestedTemplate(
        @CurrentUuid String uuid,
        @RequestBody @Valid final CreateUserSuggestedTemplateRequest request
    );

    @Operation(
        summary = "게시 알림 구독",
        description = """
            다른 사용자가 이미 요청한 영상(TRAVEL-03-003)에 대해 게시 알림을 신청합니다.
            영상이 ACCEPTED 처리되면 구독자 전체에게 FCM 알림이 발송됩니다.
            PENDING 상태인 영상에만 구독 신청이 가능합니다. ACCEPTED/DENIED 상태이면 400을 반환합니다.
            """,
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "구독 성공",
            content = @Content(
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
            responseCode = "401",
            description = "인증 실패",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "UNAUTHORIZED",
                    value = """
                        {
                          "code": "COMM-03-001",
                          "message": "인증이 필요합니다",
                          "errors": []
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "PENDING 상태가 아닌 영상 (ACCEPTED 또는 DENIED)",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "NOT_SUBSCRIBABLE_SUGGESTED_TEMPLATE",
                    value = """
                        {
                          "code": "TRAVEL-04-004",
                          "message": "이미 처리된 영상은 구독할 수 없습니다",
                          "errors": []
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 제안 템플릿",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "NOT_FOUND_SUGGESTED_TEMPLATE",
                    value = """
                        {
                          "code": "TRAVEL-02-003",
                          "message": "요청된 여행 템플릿을 찾을 수 없습니다",
                          "errors": []
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "이미 구독 중",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "ALREADY_SUBSCRIBED_SUGGESTED_TEMPLATE",
                    value = """
                        {
                          "code": "TRAVEL-03-005",
                          "message": "이미 알림 신청한 영상입니다",
                          "errors": []
                        }
                        """
                )
            )
        )
    })
    ResponseEntity<SuccessResponse<?>> subscribe(
        @PathVariable("id") Long templateId,
        @CurrentUuid String uuid
    );
}
