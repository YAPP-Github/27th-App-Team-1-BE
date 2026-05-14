package com.yapp.ndgl.domain.travel;

import java.time.LocalDateTime;
import java.util.List;

import com.yapp.ndgl.common.type.SuggestionStatus;
import com.yapp.ndgl.common.type.TravelCategory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSuggestedTemplate {

    private Long id;
    private String videoId;
    private String videoLink;
    private String recommendReason;
    private String suggesterUuid;
    private List<TravelCategory> category;
    private SuggestionStatus status;
    private LocalDateTime createdAt;

    public static UserSuggestedTemplate of(
        final String videoId,
        final String videoLink,
        final String recommendReason,
        final String suggesterUuid,
        final List<TravelCategory> category
    ) {
        return UserSuggestedTemplate.builder()
            .videoId(videoId)
            .videoLink(videoLink)
            .recommendReason(recommendReason)
            .suggesterUuid(suggesterUuid)
            .category(category)
            .status(SuggestionStatus.PENDING)
            .build();
    }
}
