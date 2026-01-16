package com.yapp.ndgl.application.auth.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(
    @Schema(description = "FCM 토큰", example = "fGhJkLmNoPqRsTuVwXyZ1234567890")
    @NotBlank(message = "FCM 토큰은 필수입니다.")
    String fcmToken,

    @Schema(description = "디바이스 모델명", example = "iPhone 14 Pro")
    String deviceModel,

    @Schema(description = "디바이스 OS", example = "iOS")
    String deviceOs,

    @Schema(description = "디바이스 OS 버전", example = "17.0")
    String deviceOsVersion,

    @Schema(description = "앱 버전", example = "1.0.0")
    String appVersion
) {

}

