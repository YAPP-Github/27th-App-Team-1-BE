package com.yapp.ndgl.domain.travel;

import com.yapp.ndgl.domain.travel.type.TravelProgramType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TravelTemplate {

    private Long id;
    private String title;
    private String travelProgramName;
    private String travelProgramProfileImage;
    private TravelProgramType travelProgramType;
    private String traveler;
    private String country;
    private String countryName;
    private String city;
    private String thumbnail;
    private String link;
    private Integer budgetPerPerson;
    private String summary;
    private Integer nights;
    private Integer days;

    public static TravelTemplate create(final String traveler, final String travelProgramName, final String travelProgramProfileImage, final TravelProgramType programType, final String country, final String countryName, final String city, final String summary, final String title, final String thumbnail, final String link, final Integer budgetPerPerson, final Integer nights, final Integer days) {
        return TravelTemplate.builder()
            .title(title)
            .travelProgramName(travelProgramName)
            .travelProgramProfileImage(travelProgramProfileImage)
            .travelProgramType(programType)
            .traveler(traveler)
            .country(country)
            .countryName(countryName)
            .city(city)
            .summary(summary)
            .thumbnail(thumbnail)
            .link(link)
            .budgetPerPerson(budgetPerPerson)
            .nights(nights)
            .days(days)
            .build();
    }
}
