package com.yapp.ndgl.domain.travel.service;

import java.util.Optional;

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
        UserSuggestedTemplateEntity templateEntity = UserSuggestedTemplateMapper.toEntity(userSuggestedTemplate);
        UserSuggestedTemplateEntity savedTemplateEntity = userSuggestedTemplateRepository.save(templateEntity);

        UserSuggestedTemplateSubscriber templateSubscriber = UserSuggestedTemplateSubscriber.of(
            savedTemplateEntity.getId(),
            templateEntity.getSuggesterUuid()
        );

        addSubscriber(templateSubscriber);

        return UserSuggestedTemplateMapper.toDomain(templateEntity);
    }

    private UserSuggestedTemplateSubscriber addSubscriber(
       final UserSuggestedTemplateSubscriber suggestedTemplateSubscriber
    ) {

        UserSuggestedTemplateSubscriberEntity entity = UserSuggestedTemplateSubscriberMapper.toEntity(
            suggestedTemplateSubscriber);
        UserSuggestedTemplateSubscriberEntity save = userSuggestedTemplateSubscriberRepository.save(entity);
        return UserSuggestedTemplateSubscriberMapper.toDomain(save);
    }

    @Transactional(readOnly = true)
    public Optional<UserSuggestedTemplate> findByVideoId(final String videoId) {
        return userSuggestedTemplateRepository.findByVideoId(videoId)
            .map(UserSuggestedTemplateMapper::toDomain);
    }
}
