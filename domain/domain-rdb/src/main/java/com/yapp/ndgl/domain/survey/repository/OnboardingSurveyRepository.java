package com.yapp.ndgl.domain.survey.repository;

import com.yapp.ndgl.domain.survey.entity.OnboardingSurveyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnboardingSurveyRepository extends JpaRepository<OnboardingSurveyEntity, Long> {

    boolean existsByUserId(Long userId);
}
