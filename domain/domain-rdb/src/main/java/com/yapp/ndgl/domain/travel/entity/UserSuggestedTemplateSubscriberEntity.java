package com.yapp.ndgl.domain.travel.entity;

import com.yapp.ndgl.domain.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "user_suggested_template_subscribers",
    uniqueConstraints = @UniqueConstraint(columnNames = {"template_id", "subscriber_uuid"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSuggestedTemplateSubscriberEntity extends BaseEntity {

    @Column(name = "template_id", nullable = false)
    private Long templateId;

    // NOTE: 사용자 시스템 정비 시 식별자 변경 가능성 있음 (UUID → User PK)
    @Column(name = "subscriber_uuid", nullable = false, length = 64)
    private String subscriberUuid;

    @Builder
    public UserSuggestedTemplateSubscriberEntity(
        final Long templateId,
        final String subscriberUuid
    ) {
        this.templateId = templateId;
        this.subscriberUuid = subscriberUuid;
    }
}
