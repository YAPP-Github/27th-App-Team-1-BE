package com.yapp.ndgl.domain.survey;

import com.yapp.ndgl.domain.survey.entity.OnboardingSurveyEntity;

public class OnboardingSurveyMapper {

  public static OnboardingSurveyEntity toEntity(final OnboardingSurvey onboardingSurvey) {
    return OnboardingSurveyEntity.builder()
        .userId(onboardingSurvey.getUserId())
        .interestRegions(onboardingSurvey.getInterestRegions())
        .contentPreferences(onboardingSurvey.getContentPreferences())
        .travelCompanions(onboardingSurvey.getTravelCompanions())
        .scheduleType(onboardingSurvey.getScheduleType())
        .build();
  }

  public static OnboardingSurvey toDomain(final OnboardingSurveyEntity entity) {
    return OnboardingSurvey.builder()
        .id(entity.getId())
        .userId(entity.getUserId())
        .interestRegions(entity.getInterestRegions())
        .contentPreferences(entity.getContentPreferences())
        .travelCompanions(entity.getTravelCompanions())
        .scheduleType(entity.getScheduleType())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }
}
