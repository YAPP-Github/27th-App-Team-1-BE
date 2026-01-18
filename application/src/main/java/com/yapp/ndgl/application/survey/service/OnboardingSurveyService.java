package com.yapp.ndgl.application.survey.service;

import com.yapp.ndgl.application.survey.controller.dto.OnboardingSurveyCreateRequest;
import com.yapp.ndgl.domain.survey.OnboardingSurvey;
import com.yapp.ndgl.domain.survey.OnboardingSurveyDomainService;
import com.yapp.ndgl.domain.user.User;
import com.yapp.ndgl.domain.user.UserDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OnboardingSurveyService {

    private final OnboardingSurveyDomainService onboardingSurveyDomainService;
    private final UserDomainService userDomainService;

    @Transactional
    public void createOnboardingSurvey(final String uuid, final OnboardingSurveyCreateRequest request) {
        User user = userDomainService.findByUuid(uuid);

        OnboardingSurvey onboardingSurvey = OnboardingSurvey.create(
            user.getId(),
            request.interestRegions(),
            request.contentPreferences(),
            request.travelCompanions(),
            request.scheduleType()
        );

        onboardingSurveyDomainService.save(onboardingSurvey);
    }
}
