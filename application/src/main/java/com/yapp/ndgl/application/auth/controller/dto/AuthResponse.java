package com.yapp.ndgl.application.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
    @Schema(description = "유저 UUID", example = "550e8400-e29b-41d4-a716-446655440000")
    String uuid,
    @Schema(description = "JWT Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String accessToken,
    @Schema(description = "닉네임", example = "행복한 여행자")
    String nickname
) {

}
