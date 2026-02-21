package com.yapp.ndgl.domain.travel.mapper;

import com.yapp.ndgl.domain.travel.TravelTemplate;
import com.yapp.ndgl.domain.travel.entity.TravelProgramEntity;
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
            .travelProgramName(programName)
            .travelProgramProfileImage(programProfileImage)
            .travelProgramType(programType)
            .traveler(entity.getTraveler())
            .country(entity.getCountry())
            .countryName(entity.getCountryName())
            .city(entity.getCity())
            .thumbnail(entity.getThumbnail())
            .link(entity.getLink())
            .budgetPerPerson(entity.getBudgetPerPerson())
            .summary(entity.getSummary())
            .title(entity.getTitle())
            .nights(entity.getNights())
            .days(entity.getDays())
            .build();
    }

    public static TravelTemplateEntity toEntity(final TravelTemplate template, final TravelProgramEntity travelProgram) {
        return TravelTemplateEntity.builder()
            .travelProgram(travelProgram)
            .traveler(template.getTraveler())
            .country(template.getCountry())
            .countryName(template.getCountryName())
            .city(template.getCity())
            .thumbnail(template.getThumbnail())
            .link(template.getLink())
            .budgetPerPerson(template.getBudgetPerPerson())
            .summary(template.getSummary())
            .title(template.getTitle())
            .nights(template.getNights())
            .days(template.getDays())
            .build();
    }
}
