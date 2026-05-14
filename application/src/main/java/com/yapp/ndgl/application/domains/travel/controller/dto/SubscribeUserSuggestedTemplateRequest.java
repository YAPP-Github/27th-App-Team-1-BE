package com.yapp.ndgl.application.domains.travel.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record SubscribeUserSuggestedTemplateRequest(
    @Schema(description = "YouTube 영상 링크", example = "https://youtu.be/abc12345678", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "영상 링크는 필수입니다.")
    String videoLink
) {
}
