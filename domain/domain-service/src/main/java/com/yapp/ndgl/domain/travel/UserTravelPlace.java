package com.yapp.ndgl.domain.travel;

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
    private String travelerTip;
    private Integer estimatedDuration;

    public static UserTravelPlace create(
        final Long userTravelId,
        final Long placeId,
        final Integer day,
        final Integer sequence,
        final String travelerTip,
        final Integer estimatedDuration
    ) {
        return UserTravelPlace.builder()
            .userTravelId(userTravelId)
            .placeId(placeId)
            .day(day)
            .sequence(sequence)
            .travelerTip(travelerTip)
            .estimatedDuration(estimatedDuration)
            .build();
    }
}
