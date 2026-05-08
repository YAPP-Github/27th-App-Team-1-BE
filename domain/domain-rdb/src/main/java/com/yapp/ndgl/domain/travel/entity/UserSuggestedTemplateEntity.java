package com.yapp.ndgl.domain.travel.entity;

import com.yapp.ndgl.common.type.DomesticRegion;
import com.yapp.ndgl.common.type.SuggestionStatus;
import com.yapp.ndgl.common.type.TravelCategory;
import com.yapp.ndgl.domain.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_suggested_templates")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSuggestedTemplateEntity extends BaseEntity {

    @Column(name = "video_id", nullable = false, length = 50)
    private String videoId;

    @Column(name = "video_link", nullable = false, length = 500)
    private String videoLink;

    @Column(name = "recommend_reason", nullable = false, length = 1000)
    private String recommendReason;

    // NOTE: 사용자 시스템 정비 시 식별자 변경 가능성 있음 (UUID → User PK)
    @Column(name = "suggester_uuid", nullable = false, length = 64)
    private String suggesterUuid;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 50)
    private TravelCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "region", length = 50)
    private DomesticRegion region;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SuggestionStatus status;

    @Builder
    public UserSuggestedTemplateEntity(
        final String videoId,
        final String videoLink,
        final String recommendReason,
        final String suggesterUuid,
        final TravelCategory category,
        final DomesticRegion region,
        final SuggestionStatus status
    ) {
        this.videoId = videoId;
        this.videoLink = videoLink;
        this.recommendReason = recommendReason;
        this.suggesterUuid = suggesterUuid;
        this.category = category;
        this.region = region;
        this.status = status == null ? SuggestionStatus.PENDING : status;
    }
}
