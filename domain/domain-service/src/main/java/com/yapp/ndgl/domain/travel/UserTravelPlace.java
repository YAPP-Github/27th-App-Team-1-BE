package com.yapp.ndgl.domain.travel;

import java.time.LocalTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserTravelPlace {

    private Long id;
    private Long userTravelId;
    private Long placeId;
    private Integer day;
    private Integer sequence;
    private String memo;
    private Double distanceKm;
    private String transportationJson;
    private LocalTime startTime;
    private Integer estimatedDuration;
    private Integer budget;

    public static UserTravelPlace create(
        final Long userTravelId,
        final Long placeId,
        final Integer day,
        final Integer sequence,
        final String memo,
        final Double distanceKm,
        final String transportationJson,
        final LocalTime startTime,
        final Integer estimatedDuration,
        final Integer budget
    ) {
        return UserTravelPlace.builder()
            .userTravelId(userTravelId)
            .placeId(placeId)
            .day(day)
            .sequence(sequence)
            .memo(memo)
            .distanceKm(distanceKm)
            .transportationJson(transportationJson)
            .startTime(startTime)
            .estimatedDuration(estimatedDuration)
            .budget(budget)
            .build();
    }
}
