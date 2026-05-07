package com.yapp.ndgl.domain.travel.mapper;

import com.yapp.ndgl.domain.travel.UserSuggestedTemplate;
import com.yapp.ndgl.domain.travel.entity.UserSuggestedTemplateEntity;

public class UserSuggestedTemplateMapper {

    public static UserSuggestedTemplate toDomain(final UserSuggestedTemplateEntity entity) {
        if (entity == null) {
            return null;
        }
        return UserSuggestedTemplate.builder()
            .id(entity.getId())
            .videoLink(entity.getVideoLink())
            .recommendReason(entity.getRecommendReason())
            .suggesterUuid(entity.getSuggesterUuid())
            .category(entity.getCategory())
            .region(entity.getRegion())
            .status(entity.getStatus())
            .build();
    }

    public static UserSuggestedTemplateEntity toEntity(final UserSuggestedTemplate domain) {
        return UserSuggestedTemplateEntity.builder()
            .videoLink(domain.getVideoLink())
            .recommendReason(domain.getRecommendReason())
            .suggesterUuid(domain.getSuggesterUuid())
            .category(domain.getCategory())
            .region(domain.getRegion())
            .status(domain.getStatus())
            .build();
    }
}
