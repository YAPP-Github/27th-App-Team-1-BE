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
}
