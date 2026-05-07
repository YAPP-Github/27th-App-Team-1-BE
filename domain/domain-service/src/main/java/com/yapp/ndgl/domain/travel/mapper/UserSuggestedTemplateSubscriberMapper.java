package com.yapp.ndgl.domain.travel.mapper;

import com.yapp.ndgl.domain.travel.UserSuggestedTemplateSubscriber;
import com.yapp.ndgl.domain.travel.entity.UserSuggestedTemplateSubscriberEntity;

public class UserSuggestedTemplateSubscriberMapper {

    public static UserSuggestedTemplateSubscriber toDomain(
        final UserSuggestedTemplateSubscriberEntity entity
    ) {
        if (entity == null) {
            return null;
        }
        return UserSuggestedTemplateSubscriber.builder()
            .id(entity.getId())
            .templateId(entity.getTemplateId())
            .subscriberUuid(entity.getSubscriberUuid())
            .build();
    }

    public static UserSuggestedTemplateSubscriberEntity toEntity(
        final UserSuggestedTemplateSubscriber domain
    ) {
        return UserSuggestedTemplateSubscriberEntity.builder()
            .templateId(domain.getTemplateId())
            .subscriberUuid(domain.getSubscriberUuid())
            .build();
    }
}
