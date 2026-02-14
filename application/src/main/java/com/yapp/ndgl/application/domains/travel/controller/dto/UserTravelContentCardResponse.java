package com.yapp.ndgl.application.domains.travel.controller.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.yapp.ndgl.domain.travel.UserTravel;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserTravelContentCardResponse(
	@Schema(description = "유저 여행 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
	Long userTravelId,
	@Schema(description = "원본 여행 템플릿 ID", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
	Long templateId,
	@Schema(description = "여행 제목", example = "도쿄 3박 4일", requiredMode = Schema.RequiredMode.REQUIRED)
	String title,
	@Schema(description = "국가", example = "JP", requiredMode = Schema.RequiredMode.REQUIRED)
	String country,
	@Schema(description = "도시", example = "도쿄", requiredMode = Schema.RequiredMode.REQUIRED)
	String city,
	@Schema(description = "여행 시작일", example = "2026-03-01", requiredMode = Schema.RequiredMode.REQUIRED)
	LocalDate startDate,
	@Schema(description = "여행 시작시간", example = "09:00:00", nullable = true)
	LocalTime startTime,
	@Schema(description = "여행 종료일", example = "2026-03-04", requiredMode = Schema.RequiredMode.REQUIRED)
	LocalDate endDate,
	@Schema(description = "박 수", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
	Integer nights,
	@Schema(description = "일 수", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
	Integer days
) {

	public static UserTravelContentCardResponse from(final UserTravel userTravel) {
		return new UserTravelContentCardResponse(
			userTravel.getId(),
			userTravel.getTemplateId(),
			userTravel.getTitle(),
			userTravel.getCountry(),
			userTravel.getCity(),
			userTravel.getStartDate(),
			userTravel.getStartTime(),
			userTravel.getEndDate(),
			userTravel.getNights(),
			userTravel.getDays()
		);
	}
}
