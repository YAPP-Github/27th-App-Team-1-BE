package com.yapp.ndgl.application.domains.travel.controller.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserTravelRequest(
	@Schema(description = "여행 제목", example = "도쿄 3박 4일", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "여행 제목은 필수입니다.")
	@Size(max = 500, message = "여행 제목은 최대 500자까지 입력할 수 있습니다.")
	String title,
	@Schema(description = "여행 시작일", example = "2026-01-23", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull(message = "여행 시작일은 필수입니다.")
	LocalDate startDate,
	@Schema(description = "여행 종료일", example = "2026-01-27", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull(message = "여행 종료일은 필수입니다.")
	LocalDate endDate
) {
}
