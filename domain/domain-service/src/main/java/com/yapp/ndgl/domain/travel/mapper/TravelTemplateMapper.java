package com.yapp.ndgl.domain.travel.mapper;

import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.entity.TravelTemplateEntity;

public class TravelTemplateMapper {

    public static TravelTemplate toDomain(final TravelTemplateEntity entity) {
        if (entity == null) {
            return null;
        }

        String name = entity.getYoutuber() == null ? null : entity.getYoutuber().getName();
        String profileImage = entity.getYoutuber() == null ? null : entity.getYoutuber().getProfileImage();

        return TravelTemplate.builder()
            .id(entity.getId())
            .travelId(entity.getTravelId())
            .youtuber(name)
            .profileImage(profileImage)
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
