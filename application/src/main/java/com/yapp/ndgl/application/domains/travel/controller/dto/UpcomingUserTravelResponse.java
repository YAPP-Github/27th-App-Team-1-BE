package com.yapp.ndgl.application.domains.travel.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record UpcomingUserTravelResponse(
    @Schema(description = "유저의 여행 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    long userTravelId,
    @Schema(description = "여행 제목", example = "여행 제목", requiredMode = Schema.RequiredMode.REQUIRED)
    String title,
    @Schema(description = "여행 국가", example = "인도", requiredMode = Schema.RequiredMode.REQUIRED)
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

    @Schema(description = "다가오는 여행 프로그램 정보", requiredMode = Schema.RequiredMode.REQUIRED)
    UserTravelPlace upcomingUserTravelPlace
) {

    public record UserTravelPlace(

        @Schema(description = "장소 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "예상 소요 시간 (분)", example = "60", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer estimatedDuration,

        @Schema(description = "장소 정보", nullable = true)
        PlaceInfo place
    ) {

        public record PlaceInfo(
            @Schema(description = "Google Places 장소 ID", example = "ChIJSc8jdZORQTURu6BMwxrKbGg", requiredMode = Schema.RequiredMode.REQUIRED)
            String googlePlaceId,
            @Schema(description = "장소 썸네일", example = "https://lh3.googleusercontent.com/place-photos/AEkURDym40I4XyqXUosRz8bTu9aPvDUklxkfM79KCa03C0SQTnDaTu_RXXiWQjCRZ3-yK4dTbzoySqMrucj1ubPQNUZ5yKseTRfmaME5C--5jLYB0rU-MLXqUabNEk3myTWywzIuEHcKz_I-H4Xtdg=s4800-w4800-h3600", nullable = true)
            String thumbnail,
            @Schema(description = "위도", example = "35.6762", requiredMode = Schema.RequiredMode.REQUIRED)
            Double latitude,
            @Schema(description = "경도", example = "139.6503", requiredMode = Schema.RequiredMode.REQUIRED)
            Double longitude,
            @Schema(description = "장소 이름", example = "도쿄타워", requiredMode = Schema.RequiredMode.REQUIRED)
            String name,
            @Schema(description = "오늘의 영업시간 정보 (JSON 파싱 실패 시 null)", example = "09:00~23:00", nullable = true)
            String regularOpeningHours,
            @Schema(description = "Google Maps URI", example = "https://maps.google.com/?cid=14776686710302251978", requiredMode = Schema.RequiredMode.REQUIRED)
            String googleMapsUri
        ) {

        }

    }
}
