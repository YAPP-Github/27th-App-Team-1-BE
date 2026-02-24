package com.yapp.ndgl.application.domains.travel.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateUserTravelPlaceRequest(
	@Schema(description = "메모", example = "오전 시간 방문 추천", nullable = true)
	@Size(max = 1000, message = "memo는 최대 1000자까지 입력할 수 있습니다.")
	String memo,
	@Schema(description = "예산(원)", example = "50000", nullable = true)
	@Min(value = 0, message = "budget은 0 이상이어야 합니다.")
	Integer budget
) {
}
