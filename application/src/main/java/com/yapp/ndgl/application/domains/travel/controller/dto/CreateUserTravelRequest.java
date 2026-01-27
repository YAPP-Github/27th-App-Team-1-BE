package com.yapp.ndgl.application.domains.travel.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateUserTravelRequest(
    @Schema(description = "여행 템플릿 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "여행 템플릿 ID는 필수입니다.")
    Long templateId,

    @Schema(description = "여행 시작일", example = "2026-01-23", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "여행 시작일은 필수입니다.")
    LocalDate startDate,

    @Schema(description = "여행 종료일", example = "2026-01-27", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "여행 종료일은 필수입니다.")
    LocalDate endDate
) {
}
