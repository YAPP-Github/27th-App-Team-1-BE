package com.yapp.ndgl.application.domains.travel.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserTravelIdResponse(
    @Schema(description = "생성된 사용자 여행 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id
) {
}
