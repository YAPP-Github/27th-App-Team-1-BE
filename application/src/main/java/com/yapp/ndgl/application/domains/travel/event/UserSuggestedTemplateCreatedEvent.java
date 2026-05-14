package com.yapp.ndgl.application.domains.travel.event;

import java.util.List;

public record UserSuggestedTemplateCreatedEvent(
	Long templateId,
	String videoId,
	String videoLink,
	String suggesterUuid,
	List<String> category,
	String recommendReason
) {
}
