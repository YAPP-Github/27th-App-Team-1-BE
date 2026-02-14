package com.yapp.ndgl.application.domains.travel.controller.dto;

import java.time.LocalTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateUserTravelPlaceStartTimesRequest(
	@Schema(description = "수정 대상 목록", requiredMode = Schema.RequiredMode.REQUIRED)
	@NotEmpty(message = "업데이트 목록은 최소 1개 이상이어야 합니다.")
	List<@Valid Item> updates
) {
	public record Item(
		@Schema(description = "유저 여행 장소 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
		@NotNull(message = "id는 필수입니다.")
		Long id,
		@Schema(description = "시작 시간", example = "09:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
		@NotNull(message = "startTime은 필수입니다.")
		LocalTime startTime
	) {
	}
}
