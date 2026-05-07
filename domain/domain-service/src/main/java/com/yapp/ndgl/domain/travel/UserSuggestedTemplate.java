package com.yapp.ndgl.domain.travel;

import com.yapp.ndgl.common.type.DomesticRegion;
import com.yapp.ndgl.common.type.SuggestionStatus;
import com.yapp.ndgl.common.type.TravelCategory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSuggestedTemplate {

    private Long id;
    private String videoLink;
    private String recommendReason;
    private String suggesterUuid;
    private TravelCategory category;
    private DomesticRegion region;
    private SuggestionStatus status;

    public static UserSuggestedTemplate of(
        final String videoLink,
        final String recommendReason,
        final String suggesterUuid,
        final TravelCategory category,
        final DomesticRegion region
    ) {
        return UserSuggestedTemplate.builder()
            .videoLink(videoLink)
            .recommendReason(recommendReason)
            .suggesterUuid(suggesterUuid)
            .category(category)
            .region(region)
            .status(SuggestionStatus.PENDING)
            .build();
    }
}
