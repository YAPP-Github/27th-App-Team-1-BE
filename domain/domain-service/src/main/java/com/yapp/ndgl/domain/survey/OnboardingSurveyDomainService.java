package com.yapp.ndgl.domain.survey;

import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.SurveyErrorCode;
import com.yapp.ndgl.domain.survey.entity.OnboardingSurveyEntity;
import com.yapp.ndgl.domain.survey.repository.OnboardingSurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingSurveyDomainService {

    private final OnboardingSurveyRepository onboardingSurveyRepository;

    @Transactional
    public OnboardingSurvey save(final OnboardingSurvey onboardingSurvey) {
        if (onboardingSurveyRepository.existsByUserId(onboardingSurvey.getUserId())) {
            throw new GlobalException(SurveyErrorCode.ALREADY_EXISTS_ONBOARDING_SURVEY);
        }

        OnboardingSurveyEntity savedEntity = onboardingSurveyRepository
            .save(OnboardingSurveyMapper.toEntity(onboardingSurvey));

        return OnboardingSurveyMapper.toDomain(savedEntity);
    }
}
