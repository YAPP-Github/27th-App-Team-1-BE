package com.yapp.ndgl.domain.travel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.domain.travel.UserSuggestedTemplate;
import com.yapp.ndgl.domain.travel.UserSuggestedTemplateSubscriber;
import com.yapp.ndgl.domain.travel.entity.UserSuggestedTemplateEntity;
import com.yapp.ndgl.domain.travel.entity.UserSuggestedTemplateSubscriberEntity;
import com.yapp.ndgl.domain.travel.mapper.UserSuggestedTemplateMapper;
import com.yapp.ndgl.domain.travel.mapper.UserSuggestedTemplateSubscriberMapper;
import com.yapp.ndgl.domain.travel.repository.UserSuggestedTemplateRepository;
import com.yapp.ndgl.domain.travel.repository.UserSuggestedTemplateSubscriberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSuggestedTemplateDomainService {

    private final UserSuggestedTemplateRepository userSuggestedTemplateRepository;
    private final UserSuggestedTemplateSubscriberRepository userSuggestedTemplateSubscriberRepository;

    @Transactional
    public UserSuggestedTemplate create(final UserSuggestedTemplate userSuggestedTemplate) {
        UserSuggestedTemplateEntity entity = UserSuggestedTemplateMapper.toEntity(userSuggestedTemplate);
        UserSuggestedTemplateEntity saved = userSuggestedTemplateRepository.save(entity);
        return UserSuggestedTemplateMapper.toDomain(saved);
    }

    @Transactional
    public UserSuggestedTemplateSubscriber addSubscriber(
        final Long templateId,
        final String subscriberUuid
    ) {
        UserSuggestedTemplateSubscriberEntity entity =
            UserSuggestedTemplateSubscriberMapper.toEntity(
                UserSuggestedTemplateSubscriber.of(templateId, subscriberUuid)
            );
        UserSuggestedTemplateSubscriberEntity saved =
            userSuggestedTemplateSubscriberRepository.save(entity);
        return UserSuggestedTemplateSubscriberMapper.toDomain(saved);
    }

    @Transactional(readOnly = true)
    public boolean existsByVideoId(final String videoId) {
        return userSuggestedTemplateRepository.existsByVideoId(videoId);
    }
}
