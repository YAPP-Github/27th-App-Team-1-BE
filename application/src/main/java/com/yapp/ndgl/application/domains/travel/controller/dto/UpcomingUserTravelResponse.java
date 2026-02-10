package com.yapp.ndgl.application.domains.travel.controller.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.travel.UserTravel;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpcomingUserTravelResponse(
    @Schema(description = "유저의 여행 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    long userTravelId,
    @Schema(description = "여행 제목", example = "여행 제목", requiredMode = Schema.RequiredMode.REQUIRED)
    String title,
    @Schema(description = "여행 국가", example = "IN", requiredMode = Schema.RequiredMode.REQUIRED)
    String country,
    @Schema(description = "여행 도시", example = "뭄바이", requiredMode = Schema.RequiredMode.REQUIRED)
    String city,
    @Schema(description = "여행 시작 날짜", example = "2023-08-01", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate startDate,
    @Schema(description = "여행 종료 날짜", example = "2023-08-10", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate endDate,
    @Schema(description = "여행 총 밤 수", example = "6", requiredMode = Schema.RequiredMode.REQUIRED)
    int nights,
    @Schema(description = "여행 총 일 수", example = "7", requiredMode = Schema.RequiredMode.REQUIRED)
    int days,

    @Schema(description = "다가오는 여행 프로그램 정보", nullable = true)
    UserTravelPlace upcomingUserTravelPlace
) {

    public static UpcomingUserTravelResponse of(
        final UserTravel upcomingTravel,
        final com.yapp.ndgl.domain.travel.UserTravelPlace upcomingPlace,
        final Place place,
        final ObjectMapper objectMapper
    ) {
        if (upcomingTravel == null) {
            return null;
        }

        return new UpcomingUserTravelResponse(
            upcomingTravel.getId(),
            upcomingTravel.getTitle(),
            upcomingTravel.getCountry(),
            upcomingTravel.getCity(),
            upcomingTravel.getStartDate(),
            upcomingTravel.getEndDate(),
            upcomingTravel.getNights(),
            upcomingTravel.getDays(),
            UserTravelPlace.of(upcomingPlace, place, objectMapper)
        );
    }

    public record UserTravelPlace(

        @Schema(description = "장소 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "예상 소요 시간 (분)", example = "60", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer estimatedDuration,

        @Schema(description = "장소 정보", nullable = true)
        PlaceInfo place
    ) {

        public static UserTravelPlace of(
            final com.yapp.ndgl.domain.travel.UserTravelPlace upcomingPlace,
            final Place place,
            final ObjectMapper objectMapper
        ) {
            if (upcomingPlace == null) {
                return null;
            }

            return new UserTravelPlace(
                upcomingPlace.getId(),
                upcomingPlace.getEstimatedDuration(),
                PlaceInfo.from(place, objectMapper)
            );
        }

    }
}
