package com.yapp.ndgl.application.survey.facade;

import com.yapp.ndgl.application.common.annotation.Facade;
import com.yapp.ndgl.application.survey.controller.dto.OnboardingSurveyCreateRequest;
import com.yapp.ndgl.application.survey.service.OnboardingSurveyService;
import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class OnboardingSurveyFacade {

    private final OnboardingSurveyService onboardingSurveyService;

    public void createOnboardingSurvey(final String uuid, final OnboardingSurveyCreateRequest request) {
        onboardingSurveyService.createOnboardingSurvey(uuid, request);
    }
}
