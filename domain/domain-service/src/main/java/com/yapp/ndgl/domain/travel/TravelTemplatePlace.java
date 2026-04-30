package com.yapp.ndgl.domain.travel;

import java.util.List;

import com.yapp.ndgl.common.type.PlanBInfo;
import com.yapp.ndgl.common.type.Transportation;

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
    private List<Transportation> transportation;
    private List<String> travelerTips;
    private List<PlanBInfo> planB;
    private Integer estimatedDuration;

    public static TravelTemplatePlace create(
        final Long travelTemplateId,
        final Long placeId,
        final int sequence,
        final int day,
        final Double distanceKm,
        final List<Transportation> transportation,
        final List<String> travelerTips,
        final List<PlanBInfo> planB,
        final Integer estimatedDuration
    ) {
        return TravelTemplatePlace.builder()
            .travelTemplateId(travelTemplateId)
            .placeId(placeId)
            .sequence(sequence)
            .day(day)
            .distanceKm(distanceKm)
            .transportation(transportation)
            .travelerTips(travelerTips)
            .planB(planB)
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
        final List<Transportation> transportation,
        final List<String> travelerTips,
        final List<PlanBInfo> planB,
        final Integer estimatedDuration
    ) {
        return TravelTemplatePlace.builder()
            .id(id)
            .travelTemplateId(travelTemplateId)
            .placeId(placeId)
            .sequence(sequence)
            .day(day)
            .distanceKm(distanceKm)
            .transportation(transportation)
            .travelerTips(travelerTips)
            .planB(planB)
            .estimatedDuration(estimatedDuration)
            .build();
    }
}
