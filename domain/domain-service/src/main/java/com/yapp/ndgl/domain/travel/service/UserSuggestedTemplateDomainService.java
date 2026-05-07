package com.yapp.ndgl.domain.travel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.domain.travel.UserSuggestedTemplate;
import com.yapp.ndgl.domain.travel.entity.UserSuggestedTemplateEntity;
import com.yapp.ndgl.domain.travel.mapper.UserSuggestedTemplateMapper;
import com.yapp.ndgl.domain.travel.repository.UserSuggestedTemplateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSuggestedTemplateDomainService {

    private final UserSuggestedTemplateRepository userSuggestedTemplateRepository;

    @Transactional
    public UserSuggestedTemplate create(final UserSuggestedTemplate userSuggestedTemplate) {
        UserSuggestedTemplateEntity entity = UserSuggestedTemplateMapper.toEntity(userSuggestedTemplate);
        UserSuggestedTemplateEntity saved = userSuggestedTemplateRepository.save(entity);
        return UserSuggestedTemplateMapper.toDomain(saved);
    }
}
