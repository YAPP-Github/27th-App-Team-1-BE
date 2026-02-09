package com.yapp.ndgl.application.domains.travel.controller.dto;

import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.type.TravelProgramType;

import io.swagger.v3.oas.annotations.media.Schema;

public record TravelTemplateRecommendationResponse(
    @Schema(description = "여행 템플릿 ID", example = "TRAVEL_001", requiredMode = Schema.RequiredMode.REQUIRED)
    String travelId,
    @Schema(description = "영상 제목", example = "도쿄 3박 4일 완벽 여행 가이드", requiredMode = Schema.RequiredMode.REQUIRED)
    String title,
    @Schema(description = "영상 썸네일 URL", example = "https://example.com/thumbnail/tokyo.jpg", nullable = true)
    String thumbnail,
    @Schema(description = "프로그램명", example = "빠니보틀", requiredMode = Schema.RequiredMode.REQUIRED)
    String programName,
    @Schema(description = "프로그램 타입", example = "YOUTUBE", requiredMode = Schema.RequiredMode.REQUIRED)
    TravelProgramType programType,
    @Schema(description = "여행자 표시명", example = "빠니보틀 Pani Bottle", nullable = true)
    String traveler,
    @Schema(description = "국가", example = "JP", requiredMode = Schema.RequiredMode.REQUIRED)
    String country,
    @Schema(description = "도시", example = "도쿄", requiredMode = Schema.RequiredMode.REQUIRED)
    String city,
    @Schema(description = "박 수", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer nights,
    @Schema(description = "일 수", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
    Integer days
) {

    public static TravelTemplateRecommendationResponse from(final TravelTemplate travelTemplate) {
        return new TravelTemplateRecommendationResponse(
            travelTemplate.getTravelId(),
            travelTemplate.getTitle(),
            travelTemplate.getThumbnail(),
            travelTemplate.getTravelProgramName(),
            travelTemplate.getTravelProgramType(),
            travelTemplate.getTraveler(),
            travelTemplate.getCountry(),
            travelTemplate.getCity(),
            travelTemplate.getNights(),
            travelTemplate.getDays()
        );
    }
}
