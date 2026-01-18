package com.yapp.ndgl.application.survey.controller.dto;

import com.yapp.ndgl.common.survey.ContentPreference;
import com.yapp.ndgl.common.survey.InterestRegion;
import com.yapp.ndgl.common.survey.ScheduleType;
import com.yapp.ndgl.common.survey.TravelCompanion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record OnboardingSurveyCreateRequest(
    @Schema(description = "관심 지역 (다중 선택)", example = "[\"DOMESTIC\", \"JAPAN\"]")
    @NotEmpty(message = "관심 지역은 최소 1개 이상 선택해야 합니다.")
    Set<InterestRegion> interestRegions,

    @Schema(description = "여행 콘텐츠 취향 (다중 선택)", example = "[\"NEW_EXPERIENCE\", \"FOOD_FOCUSED\"]")
    @NotEmpty(message = "여행 콘텐츠 취향은 최소 1개 이상 선택해야 합니다.")
    Set<ContentPreference> contentPreferences,

    @Schema(description = "동행 (다중 선택)", example = "[\"ALONE\", \"FRIENDS\"]")
    @NotEmpty(message = "동행은 최소 1개 이상 선택해야 합니다.")
    Set<TravelCompanion> travelCompanions,

    @Schema(description = "일정 스타일 (단일 선택)", example = "BALANCED")
    @NotNull(message = "일정 스타일은 필수입니다.")
    ScheduleType scheduleType
) {
}
