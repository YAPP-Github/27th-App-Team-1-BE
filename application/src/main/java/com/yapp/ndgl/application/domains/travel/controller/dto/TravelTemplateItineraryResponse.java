package com.yapp.ndgl.application.domains.travel.controller.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.common.type.TransportationMode;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.domain.travel.TravelTemplatePlace;

import io.swagger.v3.oas.annotations.media.Schema;

public record TravelTemplateItineraryResponse(
	@Schema(description = "여행 여정 목록", requiredMode = Schema.RequiredMode.REQUIRED)
	List<ItineraryPlaceResponse> itineraries
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
        @Schema(description = "이전 장소로부터의 거리 (km)", example = "2.5", nullable = true)
        Double distanceKm,
        @Schema(description = "교통수단 목록", nullable = true)
        List<TransportationInfo> transportation,
        @Deprecated
        @Schema(description = "여행자 팁 (Deprecated: travelerTips 사용 권장)", example = "저녁 시간대 방문 추천", nullable = true, deprecated = true)
        String travelerTip,
        @Schema(description = "여행자 팁 목록", example = "[\"저녁 시간대 방문 추천\", \"현지인 맛집\"]", nullable = true)
        List<String> travelerTips,
        @Schema(description = "대체 장소 목록 (Plan B)", nullable = true)
        List<PlanBInfo> planB,
        @Schema(description = "예상 소요 시간 (분)", example = "60", requiredMode = Schema.RequiredMode.REQUIRED)
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

            // travelerTipsJson 파싱
            String travelerTip = null;
            List<String> travelerTips = null;
            if (travelTemplatePlace.getTravelerTipsJson() != null) {
                try {
                    List<String> tips = objectMapper.readValue(
                        travelTemplatePlace.getTravelerTipsJson(),
                        new TypeReference<List<String>>() {}
                    );
                    travelerTips = tips;
                    travelerTip = tips.isEmpty() ? null : tips.get(0);
                } catch (Exception e) {
                    // JSON 파싱 실패 시 null
                }
            }

            // transportationJson 파싱
            List<TransportationInfo> transportation = null;
            if (travelTemplatePlace.getTransportationJson() != null) {
                try {
                    List<java.util.Map<String, Object>> transportList = objectMapper.readValue(
                        travelTemplatePlace.getTransportationJson(),
                        new TypeReference<List<java.util.Map<String, Object>>>() {}
                    );
                    transportation = transportList.stream()
                        .map(t -> {
                            String modeStr = (String) t.get("mode");
                            TransportationMode mode = modeStr != null ? TransportationMode.valueOf(modeStr) : null;
                            Integer timeMin = t.get("time_min") != null ? ((Number) t.get("time_min")).intValue() : null;
                            return new TransportationInfo(mode, timeMin);
                        })
                        .toList();
                } catch (Exception e) {
                    // JSON 파싱 실패 시 null
                }
            }

            // planBJson 파싱
            List<PlanBInfo> planB = null;
            if (travelTemplatePlace.getPlanBJson() != null) {
                try {
                    List<java.util.Map<String, String>> planBList = objectMapper.readValue(
                        travelTemplatePlace.getPlanBJson(),
                        new TypeReference<List<java.util.Map<String, String>>>() {}
                    );
                    planB = planBList.stream()
                        .map(p -> new PlanBInfo(
                            p.get("name"),
                            p.get("feature")
                        ))
                        .toList();
                } catch (Exception e) {
                    // JSON 파싱 실패 시 null
                }
            }

            return new ItineraryPlaceResponse(
                travelTemplatePlace.getId(),
                travelTemplatePlace.getDay(),
                travelTemplatePlace.getSequence(),
                travelTemplatePlace.getDistanceKm(),
                transportation,
                travelerTip,
                travelerTips,
                planB,
                travelTemplatePlace.getEstimatedDuration(),
                place == null ? null : PlaceInfo.from(place, objectMapper)
            );
        }
    }

    public record TransportationInfo(
        @Schema(description = "교통수단", example = "TAXI", requiredMode = Schema.RequiredMode.REQUIRED)
        TransportationMode mode,
        @Schema(description = "소요 시간 (분)", example = "45", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer timeMin
    ) {
    }

    public record PlanBInfo(
        @Schema(description = "대체 장소명", example = "Noi Bai Airport Lounge", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,
        @Schema(description = "특징", example = "시내로 나가기 전 간단히 허기를 채우거나 휴식을 취하기 좋은 라운지", nullable = true)
        String feature
    ) {
    }
}
