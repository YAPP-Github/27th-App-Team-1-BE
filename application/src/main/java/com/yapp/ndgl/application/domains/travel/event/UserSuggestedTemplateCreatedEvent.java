package com.yapp.ndgl.application.domains.travel.event;

public record UserSuggestedTemplateCreatedEvent(
	Long templateId,
	String videoId,
	String videoLink,
	String suggesterUuid,
	String category,
	String region,
	String recommendReason
) {
}
