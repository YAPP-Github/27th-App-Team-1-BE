package com.yapp.ndgl.application.domains.travel.controller.dto;

import java.time.LocalTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserTravelPlaceRequest(
	@Schema(description = "Google Place ID", example = "ChIJN1t_tDeuEmsRUsoyG83frY4", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotBlank(message = "googlePlaceId는 필수입니다.")
	String googlePlaceId,
	@Schema(description = "일차", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull(message = "day는 필수입니다.")
	@Min(value = 1, message = "day는 1 이상이어야 합니다.")
	Integer day,
	@Schema(description = "일차 내 순서", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotNull(message = "sequence는 필수입니다.")
	@Min(value = 1, message = "sequence는 1 이상이어야 합니다.")
	Integer sequence,
	@Schema(description = "해당 장소 시작 시간", example = "09:00:00", nullable = true)
	LocalTime startTime,
	@Schema(description = "예상 소요 시간(분)", example = "60", nullable = true)
	@Min(value = 1, message = "estimatedDuration은 1 이상이어야 합니다.")
	Integer estimatedDuration,
	@Schema(description = "예산(원)", example = "50000", nullable = true)
	@Min(value = 0, message = "budget은 0 이상이어야 합니다.")
	Integer budget,
	@Schema(description = "이전 장소로부터의 거리(km)", example = "2.5", nullable = true)
	@DecimalMin(value = "0.0", message = "distanceKm은 0 이상이어야 합니다.")
	Double distanceKm,
	@Schema(description = "교통수단 목록", nullable = true)
	List<@Valid ItineraryTransportationInfo> transportation,
	@Schema(description = "메모", example = "오전 시간 방문 추천", nullable = true)
	@Size(max = 1000, message = "memo는 최대 1000자까지 입력할 수 있습니다.")
	String memo
) {
}
