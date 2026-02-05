package com.yapp.ndgl.domain.travel.mapper;

import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.entity.TravelTemplateEntity;
import com.yapp.ndgl.domain.travel.type.TravelProgramType;

public class TravelTemplateMapper {

    public static TravelTemplate toDomain(final TravelTemplateEntity entity) {
        if (entity == null) {
            return null;
        }

        String programName = entity.getTravelProgram() == null ? null : entity.getTravelProgram().getName();
        String programProfileImage = entity.getTravelProgram() == null ? null : entity.getTravelProgram().getProfileImage();
        TravelProgramType programType = entity.getTravelProgram() == null
            ? null
            : entity.getTravelProgram().getType();

        return TravelTemplate.builder()
            .id(entity.getId())
            .travelId(entity.getTravelId())
            .travelProgramName(programName)
            .travelProgramProfileImage(programProfileImage)
            .travelProgramType(programType)
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
