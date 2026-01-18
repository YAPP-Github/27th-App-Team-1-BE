package com.yapp.ndgl.domain.travel;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TravelTemplate {

    private Long id;
    private String travelId;
    private String traveler;
    private String country;
    private String city;
    private String weatherInfo;
    private String cultureInfo;
    private String foodInfo;
    private String thumbnail;
    private String link;
    private Integer budgetPerPerson;
    private String summary;
    private String title;
    private Integer nights;
    private Integer days;
}
