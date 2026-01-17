package com.yapp.ndgl.application.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
    @Schema(description = "유저 UUID", example = "550e8400-e29b-41d4-a716-446655440000")
    @NotBlank(message = "UUID는 필수입니다.")
    String uuid
) {

}
