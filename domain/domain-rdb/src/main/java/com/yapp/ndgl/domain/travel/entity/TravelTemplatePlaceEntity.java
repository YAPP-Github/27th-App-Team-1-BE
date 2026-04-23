package com.yapp.ndgl.domain.travel.entity;

import java.util.List;

import com.yapp.ndgl.common.type.PlanBInfo;
import com.yapp.ndgl.common.type.Transportation;
import com.yapp.ndgl.domain.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(
    name = "travel_template_places",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_travel_template_place",
            columnNames = {"travel_template_id", "day", "sequence"}
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

    @Column(name = "distance_km")
    private Double distanceKm;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "transportation_json", columnDefinition = "json")
    private List<Transportation> transportation;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "traveler_tips_json", columnDefinition = "json")
    private List<String> travelerTips;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "plan_b_json", columnDefinition = "json")
    private List<PlanBInfo> planB;

    @Column(name = "estimated_duration")
    private Integer estimatedDuration;

    @Builder
    public TravelTemplatePlaceEntity(
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
        this.travelTemplateId = travelTemplateId;
        this.placeId = placeId;
        this.sequence = sequence;
        this.day = day;
        this.distanceKm = distanceKm;
        this.transportation = transportation;
        this.travelerTips = travelerTips;
        this.planB = planB;
        this.estimatedDuration = estimatedDuration;
    }
}
