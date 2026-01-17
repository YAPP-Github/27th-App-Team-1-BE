package com.yapp.ndgl.application.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yapp.ndgl.application.auth.controller.dto.AuthResponse;
import com.yapp.ndgl.application.auth.controller.dto.UserCreateRequest;
import com.yapp.ndgl.application.auth.controller.dto.UserLoginRequest;
import com.yapp.ndgl.common.response.ErrorResponse;
import com.yapp.ndgl.common.response.SuccessResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RequestMapping("/api/v1/auth")
public interface AuthApi {

    @Operation(
        summary = "신규 유저 생성",
        description = "디바이스 정보와 푸시 알림 정보를 받아 신규 유저를 생성합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(
                schema = @Schema(implementation = AuthResponse.class),
                examples = @ExampleObject(
                    name = "SUCCESS",
                    value = """
                        {
                          "code": "2000",
                          "message": "요청에 성공하였습니다.",
                          "data": {
                            "uuid": "550e8400-e29b-41d4-a716-446655440000",
                            "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                          }
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
                examples = {
                    @ExampleObject(
                        name = "VALIDATION_ERROR",
                        value = """
                            {
                              "code": "COMM-01-005",
                              "message": "유효성 검증에 실패하였습니다",
                              "errors": [
                                {
                                  "field": "fcmToken",
                                  "message": "FCM 토큰은 필수입니다."
                                }
                              ]
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
    @PostMapping("/users")
    ResponseEntity<SuccessResponse<AuthResponse>> createUser(
        @Valid @RequestBody UserCreateRequest request
    );

    @Operation(
        summary = "UUID로 로그인",
        description = "UUID를 받아 JWT Access Token을 발급합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "성공",
            content = @Content(
                schema = @Schema(implementation = AuthResponse.class),
                examples = @ExampleObject(
                    name = "SUCCESS",
                    value = """
                        {
                          "code": "2000",
                          "message": "요청에 성공하였습니다.",
                          "data": {
                            "uuid": "550e8400-e29b-41d4-a716-446655440000",
                            "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                          }
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "유저를 찾을 수 없음",
            content = @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    name = "USER_NOT_FOUND",
                    value = """
                        {
                          "code": "COMM-02-002",
                          "message": "유저를 찾을 수 없습니다",
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
                examples = {
                    @ExampleObject(
                        name = "VALIDATION_ERROR",
                        value = """
                            {
                              "code": "COMM-01-005",
                              "message": "유효성 검증에 실패하였습니다",
                              "errors": [
                                {
                                  "field": "uuid",
                                  "message": "UUID는 필수입니다."
                                }
                              ]
                            }
                            """
                    )
                }
            )
        )
    })
    @PostMapping("/login")
    ResponseEntity<SuccessResponse<AuthResponse>> login(
        @Valid @RequestBody UserLoginRequest request
    );
}
