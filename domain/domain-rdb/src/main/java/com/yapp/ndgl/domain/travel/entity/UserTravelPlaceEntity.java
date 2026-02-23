package com.yapp.ndgl.domain.travel.entity;

import java.time.LocalTime;

import com.yapp.ndgl.domain.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "user_travel_places",
    indexes = {
        @Index(name = "idx_user_travel_id", columnList = "user_travel_id"),
        @Index(name = "idx_place_id", columnList = "place_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_travel_place",
            columnNames = {"user_travel_id", "day", "sequence"}
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTravelPlaceEntity extends BaseEntity {

    @Column(name = "user_travel_id", nullable = false)
    private Long userTravelId;

    @Column(name = "place_id", nullable = false)
    private Long placeId;

    @Column(name = "day", nullable = false)
    private Integer day;

    @Column(name = "sequence", nullable = false)
    private Integer sequence;

    @Column(name = "traveler_tip", length = 1000)
    private String travelerTip;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "estimated_duration")
    private Integer estimatedDuration;

    @Column(name = "budget")
    private Integer budget;

    @Builder
    public UserTravelPlaceEntity(
        final Long userTravelId,
        final Long placeId,
        final Integer day,
        final Integer sequence,
        final String travelerTip,
        final LocalTime startTime,
        final Integer estimatedDuration,
        final Integer budget
    ) {
        this.userTravelId = userTravelId;
        this.placeId = placeId;
        this.day = day;
        this.sequence = sequence;
        this.travelerTip = travelerTip;
        this.startTime = startTime;
        this.estimatedDuration = estimatedDuration;
        this.budget = budget;
    }

    public void updateStartTime(final LocalTime startTime) {
        this.startTime = startTime;
    }

    public void updateTravelerTipAndBudget(final String travelerTip, final Integer budget) {
        this.travelerTip = travelerTip;
        this.budget = budget;
    }
}
