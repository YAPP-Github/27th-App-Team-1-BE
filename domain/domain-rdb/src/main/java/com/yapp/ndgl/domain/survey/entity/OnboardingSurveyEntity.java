package com.yapp.ndgl.domain.survey.entity;

import com.yapp.ndgl.common.survey.ContentPreference;
import com.yapp.ndgl.common.survey.InterestRegion;
import com.yapp.ndgl.common.survey.ScheduleType;
import com.yapp.ndgl.common.survey.TravelCompanion;
import com.yapp.ndgl.domain.common.entity.BaseEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "onboarding_surveys")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OnboardingSurveyEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "onboarding_surveys_interest_regions", joinColumns = @JoinColumn(name = "onboarding_survey_id"))
    @Column(name = "interest_region")
    @Enumerated(EnumType.STRING)
    private Set<InterestRegion> interestRegions;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "onboarding_surveys_content_preferences", joinColumns = @JoinColumn(name = "onboarding_survey_id"))
    @Column(name = "content_preference")
    @Enumerated(EnumType.STRING)
    private Set<ContentPreference> contentPreferences;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "onboarding_surveys_travel_companions", joinColumns = @JoinColumn(name = "onboarding_survey_id"))
    @Column(name = "travel_companion")
    @Enumerated(EnumType.STRING)
    private Set<TravelCompanion> travelCompanions;

    @Column(name = "schedule_type")
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @Builder
    public OnboardingSurveyEntity(
        final Long userId,
        final Set<InterestRegion> interestRegions,
        final Set<ContentPreference> contentPreferences,
        final Set<TravelCompanion> travelCompanions,
        final ScheduleType scheduleType
    ) {
        this.userId = userId;
        this.interestRegions = interestRegions;
        this.contentPreferences = contentPreferences;
        this.travelCompanions = travelCompanions;
        this.scheduleType = scheduleType;
    }
}
