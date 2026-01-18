package com.yapp.ndgl.application.survey.controller;

import com.yapp.ndgl.application.auth.annotation.CurrentUuid;
import com.yapp.ndgl.application.survey.controller.dto.OnboardingSurveyCreateRequest;
import com.yapp.ndgl.application.survey.facade.OnboardingSurveyFacade;
import com.yapp.ndgl.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Survey", description = "설문조사 관련 API")
@RequiredArgsConstructor
@RestController
public class SurveyController implements SurveyApi {

    private final OnboardingSurveyFacade onboardingSurveyFacade;

    @Override
    public ResponseEntity<SuccessResponse> createOnboardingSurvey(
        @CurrentUuid String uuid,
        @Valid @RequestBody OnboardingSurveyCreateRequest request) {
        onboardingSurveyFacade.createOnboardingSurvey(uuid, request);
        return ResponseEntity.ok(SuccessResponse.noContent());
    }
}
