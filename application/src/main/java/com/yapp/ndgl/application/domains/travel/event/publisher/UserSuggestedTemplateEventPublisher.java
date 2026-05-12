package com.yapp.ndgl.application.domains.travel.event.publisher;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.yapp.ndgl.application.domains.travel.controller.dto.CreateUserSuggestedTemplateRequest;
import com.yapp.ndgl.application.domains.travel.event.UserSuggestedTemplateCreatedEvent;

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
		String category = request.category() != null ? request.category().name() : null;
		String region = request.region() != null ? request.region().name() : null;

		UserSuggestedTemplateCreatedEvent event = new UserSuggestedTemplateCreatedEvent(
			templateId,
			videoId,
			request.videoLink(),
			uuid,
			category,
			region,
			request.recommendReason()
		);

		applicationEventPublisher.publishEvent(event);
	}
}
