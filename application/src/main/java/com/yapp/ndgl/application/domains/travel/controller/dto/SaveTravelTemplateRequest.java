package com.yapp.ndgl.application.domains.travel.controller.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SaveTravelTemplateRequest(
    @Schema(description = "여행자", example = "빠니보틀", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "여행자는 필수입니다.")
    String traveler,

    @Schema(description = "여행 요약", example = "방콕 3박 4일 여행", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "여행 요약은 필수입니다.")
    String summary,

    @Schema(description = "1인당 예산", example = "1200000")
    @JsonProperty("budget_per_person")
    Integer budgetPerPerson,

    @Schema(description = "대륙", example = "아시아")
    String continent,

    @Schema(description = "국가", example = "TH", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "국가는 필수입니다.")
    String country,

    @Schema(description = "도시", example = "방콕", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "도시는 필수입니다.")
    String city,

    @Schema(description = "일정 목록", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "일정 목록은 필수입니다.")
    @Valid
    List<ItineraryRequest> itinerary
) {

    public record ItineraryRequest(
        @Schema(description = "일차", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "일차는 필수입니다.")
        Integer day,

        @Schema(description = "활동 목록", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "활동 목록은 필수입니다.")
        @Valid
        List<ActivityRequest> activities,

        @Schema(description = "교통 팁", example = "BTS 스카이트레인을 이용하면 편리합니다.")
        @JsonProperty("transportation_tip")
        String transportationTip
    ) {
    }

    public record ActivityRequest(
        @Schema(description = "순서", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "순서는 필수입니다.")
        Integer sequence,

        @Schema(description = "장소명", example = "왓 아룬", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "장소명은 필수입니다.")
        @JsonProperty("place_name")
        String placeName,

        @Schema(description = "예상 소요 시간(분)", example = "60")
        @JsonProperty("estimated_time")
        Integer estimatedTime,

        @Schema(description = "거리(km)", example = "2.5")
        @JsonProperty("distance_km")
        Double distanceKm,

        @Schema(description = "교통수단 목록")
        @Valid
        List<TransportationRequest> transportation,

        @Schema(description = "유튜브 팁 목록")
        @JsonProperty("youtube_tips")
        List<String> youtubeTips,

        @Schema(description = "대안 장소 목록")
        @Valid
        @JsonProperty("plan_b")
        List<PlanBRequest> planB
    ) {
    }

    public record TransportationRequest(
        @Schema(description = "교통수단", example = "DRIVING")
        String mode,

        @Schema(description = "소요 시간(분)", example = "15")
        @JsonProperty("time_min")
        Integer timeMin
    ) {
    }

    public record PlanBRequest(
        @Schema(description = "장소명", example = "요요기 공원", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "대안 장소명은 필수입니다.")
        String name,

        @Schema(description = "장소 특징", example = "메이지 신궁 옆에 위치한 넓은 공원으로 피크닉하기 좋음")
        String feature
    ) {
    }
}
