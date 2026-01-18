package com.yapp.ndgl.domain.travel;

import com.yapp.ndgl.domain.travel.entity.TravelTemplateEntity;

public class TravelTemplateMapper {

    public static TravelTemplate toDomain(TravelTemplateEntity entity) {
        if (entity == null) {
            return null;
        }

        return TravelTemplate.builder()
            .id(entity.getId())
            .travelId(entity.getTravelId())
            .traveler(entity.getTraveler())
            .country(entity.getCountry())
            .city(entity.getCity())
            .weatherInfo(entity.getWeatherInfo())
            .cultureInfo(entity.getCultureInfo())
            .foodInfo(entity.getFoodInfo())
            .thumbnail(entity.getThumbnail())
            .link(entity.getLink())
            .budgetPerPerson(entity.getBudgetPerPerson())
            .summary(entity.getSummary())
            .title(entity.getTitle())
            .nights(entity.getNights())
            .days(entity.getDays())
            .build();
    }
}
