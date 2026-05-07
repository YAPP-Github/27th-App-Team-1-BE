package com.yapp.ndgl.application.domains.travel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.yapp.ndgl.application.domains.auth.annotation.CurrentUuid;
import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserSuggestedTemplateRequest;
import com.yapp.ndgl.application.domains.travel.controller.dto.UserSuggestedTemplateResponse;
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
        description = "사용자가 YouTube 영상 링크와 추천 이유를 입력하여 여행 템플릿을 제안합니다. 등록 시 상태는 PENDING으로 설정되며, 영상 URL 형식 유효성만 검증합니다.",
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
            description = "등록 성공",
            content = @Content(
                examples = @ExampleObject(
                    name = "SUCCESS",
                    value = """
                        {
                          "code": "2000",
                          "message": "요청에 성공하였습니다.",
                          "data": {
                            "id": 1,
                            "videoLink": "https://youtu.be/abc12345678",
                            "recommendReason": "산정호수 일출이 정말 아름다워요. 새벽 5시에 가면 혼자 볼 수 있어요.",
                            "suggesterUuid": "uuid-1234-5678",
                            "category": "UNCATEGORIZED",
                            "region": "UNDEFINED",
                            "status": "PENDING"
                          }
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
                          "code": "COMM-03-001",
                          "message": "인증이 필요합니다",
                          "errors": []
                        }
                        """
                )
            )
        )
    })
    ResponseEntity<SuccessResponse<UserSuggestedTemplateResponse>> createUserSuggestedTemplate(
        @CurrentUuid String uuid,
        @RequestBody @Valid final CreateUserSuggestedTemplateRequest request
    );
}
