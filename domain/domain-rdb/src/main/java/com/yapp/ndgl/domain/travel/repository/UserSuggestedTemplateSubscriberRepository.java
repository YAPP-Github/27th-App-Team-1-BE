package com.yapp.ndgl.domain.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yapp.ndgl.domain.travel.entity.UserSuggestedTemplateSubscriberEntity;

public interface UserSuggestedTemplateSubscriberRepository
    extends JpaRepository<UserSuggestedTemplateSubscriberEntity, Long> {

    boolean existsByTemplateIdAndSubscriberUuid(Long templateId, String subscriberUuid);
}
