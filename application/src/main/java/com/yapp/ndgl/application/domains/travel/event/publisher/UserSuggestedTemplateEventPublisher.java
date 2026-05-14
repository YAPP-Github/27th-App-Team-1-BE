package com.yapp.ndgl.application.domains.travel.event.publisher;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserSuggestedTemplateRequest;
import com.yapp.ndgl.application.domains.travel.event.UserSuggestedTemplateCreatedEvent;
import com.yapp.ndgl.common.type.TravelCategory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserSuggestedTemplateEventPublisher {

	private final ApplicationEventPublisher applicationEventPublisher;

	public void publish(
		final Long templateId,
		final String videoId,
		final String uuid,
		final CreateUserSuggestedTemplateRequest request
	) {
		List<String> category = request.category().stream()
			.map(TravelCategory::name)
			.toList();

		UserSuggestedTemplateCreatedEvent event = new UserSuggestedTemplateCreatedEvent(
			templateId,
			videoId,
			request.videoLink(),
			uuid,
			category,
			request.recommendReason()
		);

		applicationEventPublisher.publishEvent(event);
	}
}
