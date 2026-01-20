package com.yapp.ndgl.application.domains.survey.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.domains.survey.controller.dto.OnboardingSurveyCreateRequest;
import com.yapp.ndgl.application.domains.survey.service.OnboardingSurveyService;
import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class OnboardingSurveyFacade {

    private final OnboardingSurveyService onboardingSurveyService;

    public void createOnboardingSurvey(final String uuid, final OnboardingSurveyCreateRequest request) {
        onboardingSurveyService.createOnboardingSurvey(uuid, request);
    }
}
