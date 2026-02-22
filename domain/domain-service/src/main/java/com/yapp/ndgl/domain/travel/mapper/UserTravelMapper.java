package com.yapp.ndgl.domain.travel.mapper;

import com.yapp.ndgl.domain.travel.UserTravel;
import com.yapp.ndgl.domain.travel.entity.UserTravelEntity;

public class UserTravelMapper {

    public static UserTravelEntity toEntity(final UserTravel userTravel) {
        if (userTravel == null) {
            return null;
        }

        return UserTravelEntity.builder()
            .userId(userTravel.getUserId())
            .templateId(userTravel.getTemplateId())
            .title(userTravel.getTitle())
            .country(userTravel.getCountry())
            .countryName(userTravel.getCountryName())
            .city(userTravel.getCity())
            .startDate(userTravel.getStartDate())
            .endDate(userTravel.getEndDate())
            .nights(userTravel.getNights())
            .days(userTravel.getDays())
            .thumbnail(userTravel.getThumbnail())
            .build();
    }

    public static UserTravel toDomain(final UserTravelEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserTravel.builder()
            .id(entity.getId())
            .userId(entity.getUserId())
            .templateId(entity.getTemplateId())
            .title(entity.getTitle())
            .country(entity.getCountry())
            .countryName(entity.getCountryName())
            .city(entity.getCity())
            .startDate(entity.getStartDate())
            .endDate(entity.getEndDate())
            .nights(entity.getNights())
            .days(entity.getDays())
            .thumbnail(entity.getThumbnail())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
