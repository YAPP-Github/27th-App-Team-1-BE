package com.yapp.ndgl.domain.survey;

import com.yapp.ndgl.common.survey.ContentPreference;
import com.yapp.ndgl.common.survey.InterestRegion;
import com.yapp.ndgl.common.survey.ScheduleType;
import com.yapp.ndgl.common.survey.TravelCompanion;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class OnboardingSurvey {

    private Long id;
    private Long userId;
    private Set<InterestRegion> interestRegions;
    private Set<ContentPreference> contentPreferences;
    private Set<TravelCompanion> travelCompanions;
    private ScheduleType scheduleType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OnboardingSurvey create(
        Long userId,
        Set<InterestRegion> interestRegions,
        Set<ContentPreference> contentPreferences,
        Set<TravelCompanion> travelCompanions,
        ScheduleType scheduleType
    ) {
        LocalDateTime now = LocalDateTime.now();

        return OnboardingSurvey.builder()
            .userId(userId)
            .interestRegions(interestRegions)
            .contentPreferences(contentPreferences)
            .travelCompanions(travelCompanions)
            .scheduleType(scheduleType)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }
}
