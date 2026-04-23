package com.yapp.ndgl.domain.travel;

import java.time.LocalTime;
import java.util.List;

import com.yapp.ndgl.common.type.Transportation;

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
    private List<Transportation> transportation;
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
        final List<Transportation> transportation,
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
            .transportation(transportation)
            .startTime(startTime)
            .estimatedDuration(estimatedDuration)
            .budget(budget)
            .build();
    }
}
