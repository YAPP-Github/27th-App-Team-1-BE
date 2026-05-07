package com.yapp.ndgl.domain.travel;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSuggestedTemplateSubscriber {

    private Long id;
    private Long templateId;
    private String subscriberUuid;

    public static UserSuggestedTemplateSubscriber of(
        final Long templateId,
        final String subscriberUuid
    ) {
        return UserSuggestedTemplateSubscriber.builder()
            .templateId(templateId)
            .subscriberUuid(subscriberUuid)
            .build();
    }
}
