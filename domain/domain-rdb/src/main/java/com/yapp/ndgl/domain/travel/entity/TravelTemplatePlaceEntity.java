package com.yapp.ndgl.domain.travel.entity;

import com.yapp.ndgl.domain.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "travel_template_places",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_travel_template_place",
            columnNames = {"travel_template_id", "place_id"}
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelTemplatePlaceEntity extends BaseEntity {

    @Column(name = "travel_template_id", nullable = false)
    private Long travelTemplateId;

    @Column(name = "place_id", nullable = false)
    private Long placeId;

    @Column(name = "sequence", nullable = false)
    private int sequence;

    @Column(name = "day", nullable = false)
    private int day;

    @Column(name = "traveler_tip", length = 1000)
    private String travelerTip;

    @Builder
    public TravelTemplatePlaceEntity(
        final Long travelTemplateId,
        final Long placeId,
        final int sequence,
        final int day,
        final String travelerTip
    ) {
        this.travelTemplateId = travelTemplateId;
        this.placeId = placeId;
        this.sequence = sequence;
        this.day = day;
        this.travelerTip = travelerTip;
    }
}
