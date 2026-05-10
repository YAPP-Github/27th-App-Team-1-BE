package com.yapp.ndgl.domain.travel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yapp.ndgl.common.type.SuggestionStatus;
import com.yapp.ndgl.domain.travel.entity.UserSuggestedTemplateEntity;

public interface UserSuggestedTemplateRepository
    extends JpaRepository<UserSuggestedTemplateEntity, Long> {

    Optional<UserSuggestedTemplateEntity> findFirstByVideoIdAndStatus(String videoId, SuggestionStatus status);
}
