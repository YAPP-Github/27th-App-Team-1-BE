package com.yapp.ndgl.application.domains.travel.controller.dto;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yapp.ndgl.common.exception.CommonErrorCode;
import com.yapp.ndgl.common.exception.GlobalException;
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

    private static final Logger log = LoggerFactory.getLogger(TravelTemplateItineraryResponse.class);

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
                    List<Map<String, Object>> transportList = objectMapper.readValue(
                        travelTemplatePlace.getTransportationJson(),
                        new TypeReference<List<Map<String, Object>>>() {}
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

            // planBJson 파싱 - placeId로 Place 조회하여 필드 보강
            List<PlanBInfo> planB = null;
            if (travelTemplatePlace.getPlanBJson() != null) {
                try {
                    List<Map<String, Object>> planBList = objectMapper.readValue(
                        travelTemplatePlace.getPlanBJson(),
                        new TypeReference<List<Map<String, Object>>>() {}
                    );
                    List<PlanBInfo> parsed = planBList.stream()
                        .map(p -> {
                            Long placeId = p.get("placeId") != null ? ((Number) p.get("placeId")).longValue() : null;
                            Place planBPlace = placeId != null ? placeMap.get(placeId) : null;
                            if (planBPlace == null) {
                                log.warn("PlanB 장소 조회 실패. placeId={}, templatePlaceId={}", placeId, travelTemplatePlace.getId());
                                return null;
                            }
                            return new PlanBInfo(
                                planBPlace.getGooglePlaceId(),
                                planBPlace.getName(),
                                planBPlace.getThumbnail(),
                                planBPlace.getCategory() != null ? planBPlace.getCategory().name() : null
                            );
                        })
                        .filter(Objects::nonNull)
                        .toList();
                    planB = parsed.isEmpty() ? null : parsed;
                } catch (Exception e) {
                    log.error(e.getMessage());
                    throw new GlobalException(CommonErrorCode.INTERNAL_SERVER_ERROR);
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
        @Schema(description = "Google Place ID", example = "ChIJN1t_tDeuEmsRUsoyG83frY4", requiredMode = Schema.RequiredMode.REQUIRED)
        String googlePlaceId,
        @Schema(description = "장소명", example = "콜로세움 (Colosseo)", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,
        @Schema(description = "장소 썸네일 이미지 URL", example = "https://places.googleapis.com/v1/places/ChIJN1t_tDeuEmsRUsoyG83frY4/photos/...", nullable = true)
        String thumbnail,
        @Schema(description = "장소 카테고리", example = "TOURIST_ATTRACTION", nullable = true)
        String category
    ) {
    }
}
