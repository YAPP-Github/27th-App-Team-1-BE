package com.yapp.ndgl.domain.travel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.yapp.ndgl.common.type.SuggestionStatus;
import com.yapp.ndgl.domain.travel.entity.UserSuggestedTemplateEntity;

public interface UserSuggestedTemplateRepositoryCustom {

    Page<UserSuggestedTemplateEntity> findByStatus(SuggestionStatus status, Pageable pageable);
}
