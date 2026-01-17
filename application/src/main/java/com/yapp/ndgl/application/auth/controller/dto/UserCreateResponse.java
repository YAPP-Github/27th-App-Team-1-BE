package com.yapp.ndgl.application.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserCreateResponse(
    @Schema(description = "생성된 유저의 UUID", example = "550e8400-e29b-41d4-a716-446655440000")
    String uuid,
    @Schema(description = "JWT Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String accessToken
) {

}
