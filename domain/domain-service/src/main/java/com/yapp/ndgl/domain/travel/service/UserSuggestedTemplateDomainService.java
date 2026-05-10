package com.yapp.ndgl.domain.travel.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.TravelErrorCode;
import com.yapp.ndgl.common.type.SuggestionStatus;
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

        return UserSuggestedTemplateMapper.toDomain(savedTemplateEntity);
    }

    private UserSuggestedTemplateSubscriber addSubscriber(
       final UserSuggestedTemplateSubscriber suggestedTemplateSubscriber
    ) {

        UserSuggestedTemplateSubscriberEntity entity = UserSuggestedTemplateSubscriberMapper.toEntity(
            suggestedTemplateSubscriber);
        UserSuggestedTemplateSubscriberEntity save = userSuggestedTemplateSubscriberRepository.save(entity);
        return UserSuggestedTemplateSubscriberMapper.toDomain(save);
    }

    @Transactional
    public void subscribe(final Long templateId, final String subscriberUuid) {
        UserSuggestedTemplateEntity template = userSuggestedTemplateRepository.findById(templateId)
            .orElseThrow(() -> new GlobalException(TravelErrorCode.NOT_FOUND_SUGGESTED_TEMPLATE));

        if (template.getStatus() != SuggestionStatus.PENDING) {
            throw new GlobalException(TravelErrorCode.NOT_SUBSCRIBABLE_SUGGESTED_TEMPLATE);
        }
        if (userSuggestedTemplateSubscriberRepository.existsByTemplateIdAndSubscriberUuid(templateId, subscriberUuid)) {
            throw new GlobalException(TravelErrorCode.ALREADY_SUBSCRIBED_SUGGESTED_TEMPLATE);
        }
        addSubscriber(UserSuggestedTemplateSubscriber.of(templateId, subscriberUuid));
    }

    @Transactional(readOnly = true)
    public Optional<UserSuggestedTemplate> findByVideoIdAndStatus(final String videoId, final SuggestionStatus status) {
        return userSuggestedTemplateRepository.findFirstByVideoIdAndStatus(videoId, status)
            .map(UserSuggestedTemplateMapper::toDomain);
    }
}
