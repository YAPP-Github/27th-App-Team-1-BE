package com.yapp.ndgl.domain.travel;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TravelTemplatePlace {

    private Long id;
    private Long travelTemplateId;
    private Long placeId;
    private int sequence;
    private int day;
    private Double distanceKm;
    private String transportationJson;
    private String youtubeTipsJson;
    private String planBJson;
    private Integer estimatedDuration;

    public static TravelTemplatePlace create(
        final Long travelTemplateId,
        final Long placeId,
        final int sequence,
        final int day,
        final Double distanceKm,
        final String transportationJson,
        final String youtubeTipsJson,
        final String planBJson,
        final Integer estimatedDuration
    ) {
        return TravelTemplatePlace.builder()
            .travelTemplateId(travelTemplateId)
            .placeId(placeId)
            .sequence(sequence)
            .day(day)
            .distanceKm(distanceKm)
            .transportationJson(transportationJson)
            .youtubeTipsJson(youtubeTipsJson)
            .planBJson(planBJson)
            .estimatedDuration(estimatedDuration)
            .build();
    }

    public static TravelTemplatePlace createWithId(
        final Long id,
        final Long travelTemplateId,
        final Long placeId,
        final int sequence,
        final int day,
        final Double distanceKm,
        final String transportationJson,
        final String youtubeTipsJson,
        final String planBJson,
        final Integer estimatedDuration
    ) {
        return TravelTemplatePlace.builder()
            .id(id)
            .travelTemplateId(travelTemplateId)
            .placeId(placeId)
            .sequence(sequence)
            .day(day)
            .distanceKm(distanceKm)
            .transportationJson(transportationJson)
            .youtubeTipsJson(youtubeTipsJson)
            .planBJson(planBJson)
            .estimatedDuration(estimatedDuration)
            .build();
    }
}
