package com.yapp.ndgl.application.domains.travel.controller.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;

import io.swagger.v3.oas.annotations.media.Schema;

public record TravelTemplateItineraryResponse(
	@Schema(description = "여행 장소 목록")
	List<ItineraryPlaceResponse> places
) {
    public static TravelTemplateItineraryResponse of(
        final List<TravelTemplatePlace> travelTemplatePlaces,
        final Map<Long, Place> placeMap,
        final ObjectMapper objectMapper
    ) {
        List<ItineraryPlaceResponse> places = travelTemplatePlaces.stream()
            .map(travelTemplatePlace -> ItineraryPlaceResponse.of(travelTemplatePlace, placeMap, objectMapper))
            .toList();

        return new TravelTemplateItineraryResponse(places);
    }

    public record ItineraryPlaceResponse(
        @Schema(description = "장소 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,
        @Schema(description = "일차", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer day,
        @Schema(description = "순서", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer sequence,
        @Schema(description = "여행자 팁", example = "저녁 시간대 방문 추천", nullable = true)
        String travelerTip,
        @Schema(description = "예상 소요 시간 (분)", example = "60", nullable = true)
        Integer estimatedDuration,
        @Schema(description = "장소 정보", nullable = true)
        PlaceInfo place
    ) {
        public static ItineraryPlaceResponse of(
            final TravelTemplatePlace travelTemplatePlace,
            final Map<Long, Place> placeMap,
            final ObjectMapper objectMapper
        ) {
            Place place = placeMap.get(travelTemplatePlace.getPlaceId());

            return new ItineraryPlaceResponse(
                travelTemplatePlace.getId(),
                travelTemplatePlace.getDay(),
                travelTemplatePlace.getSequence(),
                travelTemplatePlace.getTravelerTip(),
                travelTemplatePlace.getEstimatedDuration(),
                place == null ? null : PlaceInfo.from(place, objectMapper)
            );
        }
    }

    public record PlaceInfo(
        @Schema(description = "장소 ID", example = "ChIJSc8jdZORQTURu6BMwxrKbGg", requiredMode = Schema.RequiredMode.REQUIRED)
        String placeId,
        @Schema(description = "위도", example = "35.6762", requiredMode = Schema.RequiredMode.REQUIRED)
        Double latitude,
        @Schema(description = "경도", example = "139.6503", requiredMode = Schema.RequiredMode.REQUIRED)
        Double longitude,
        @Schema(description = "장소 이름", example = "도쿄타워", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,
        @Schema(description = "오늘의 영업시간 정보 (JSON 파싱 실패 시 null)", example = "09:00~23:00", nullable = true)
        String regularOpeningHours
    ) {

        public static PlaceInfo from(final Place place, final ObjectMapper objectMapper) {
            String todayOpeningHours = parseTodayOpeningHours(place.getRegularOpeningHours(), objectMapper);

            return new PlaceInfo(
                place.getPlaceId(),
                place.getLatitude(),
                place.getLongitude(),
                place.getName(),
                todayOpeningHours
            );
        }

        private static String parseTodayOpeningHours(final String regularOpeningHoursJson, final ObjectMapper objectMapper) {
            if (regularOpeningHoursJson == null || regularOpeningHoursJson.isEmpty()) {
                return null;
            }

            try {
                List<String> weekdayDescriptions = objectMapper.readValue(
                    regularOpeningHoursJson,
                    new TypeReference<>() {}
                );

                if (weekdayDescriptions.isEmpty()) {
                    return null;
                }

                // 오늘 요일 index 계산 (0: 월요일, 1: 화요일, ..., 6: 일요일)
                int todayIndex = LocalDate.now().getDayOfWeek().getValue() - 1;

                if (todayIndex < 0 || todayIndex >= weekdayDescriptions.size()) {
                    return null;
                }

                String todayDescription = weekdayDescriptions.get(todayIndex);
                int colonIndex = todayDescription.indexOf(':');
                if (colonIndex < 0) {
                    return todayDescription;
                }

                String openingHours = todayDescription.substring(colonIndex + 1).trim();
                return openingHours.isEmpty() ? null : openingHours;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
