package com.yapp.ndgl.application.domains.travel.controller.dto;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yapp.ndgl.common.exception.CommonErrorCode;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.type.PlanBInfo;
import com.yapp.ndgl.common.type.Transportation;
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
        final Map<String, Place> nearbyPlaceMap
    ) {
        List<ItineraryPlaceResponse> places = travelTemplatePlaces.stream()
            .map(travelTemplatePlace -> ItineraryPlaceResponse.of(travelTemplatePlace, placeMap, nearbyPlaceMap))
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
        List<ItineraryTransportationInfo> transportation,
        @Deprecated
        @Schema(description = "여행자 팁 (Deprecated: travelerTips 사용 권장)", example = "저녁 시간대 방문 추천", nullable = true, deprecated = true)
        String travelerTip,
        @Schema(description = "여행자 팁 목록", example = "[\"저녁 시간대 방문 추천\", \"현지인 맛집\"]", nullable = true)
        List<String> travelerTips,
        @Deprecated
        @Schema(description = "대체 장소 목록 (Plan B)", nullable = true)
        List<ItineraryPlanBInfo> planB,
        @Schema(description = "예상 소요 시간 (분)", example = "60", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer estimatedDuration,
        @Schema(description = "장소 정보", nullable = true)
        PlaceInfo place
    ) {
        public static ItineraryPlaceResponse of(
            final TravelTemplatePlace travelTemplatePlace,
            final Map<Long, Place> placeMap,
            final Map<String, Place> nearbyPlaceMap
        ) {
            Place place = placeMap.get(travelTemplatePlace.getPlaceId());

            List<String> travelerTips = travelTemplatePlace.getTravelerTips();
            String travelerTip = (travelerTips != null && !travelerTips.isEmpty()) ? travelerTips.get(0) : null;

            List<ItineraryTransportationInfo> transportation = null;
            if (travelTemplatePlace.getTransportation() != null) {
                transportation = travelTemplatePlace.getTransportation().stream()
                    .map(t -> new ItineraryTransportationInfo(t.mode(), t.timeMin()))
                    .toList();
            }

            List<ItineraryPlanBInfo> planB = null;
            if (travelTemplatePlace.getPlanB() != null) {
                try {
                    List<ItineraryPlanBInfo> parsed = travelTemplatePlace.getPlanB().stream()
                        .map(p -> {
                            Place planBPlace = p.placeId() != null ? placeMap.get(p.placeId()) : null;
                            if (planBPlace == null) {
                                log.warn("PlanB 장소 조회 실패. placeId={}, templatePlaceId={}", p.placeId(), travelTemplatePlace.getId());
                                return null;
                            }
                            return new ItineraryPlanBInfo(
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
                place == null ? null : PlaceInfo.from(place, nearbyPlaceMap)
            );
        }
    }
}
