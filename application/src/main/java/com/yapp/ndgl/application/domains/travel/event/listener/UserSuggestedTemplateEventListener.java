package com.yapp.ndgl.application.domains.travel.event.listener;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.yapp.ndgl.application.domains.travel.event.UserSuggestedTemplateCreatedEvent;
import com.yapp.ndgl.application.domains.travel.event.UserSuggestedTemplateNotification;
import com.yapp.ndgl.clients.discord.DiscordNotifier;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserSuggestedTemplateEventListener {

	private final DiscordNotifier discordNotifier;

	@Async
	@EventListener
	public void handleUserSuggestedTemplateCreatedEvent(final UserSuggestedTemplateCreatedEvent event) {
		try {
			UserSuggestedTemplateNotification notification = UserSuggestedTemplateNotification.from(event);
			discordNotifier.notify(notification);
		} catch (Exception e) {
			log.error("사용자 제안 컨텐츠 Discord 알림 처리 중 오류가 발생했습니다. templateId={}", event.templateId(), e);
		}
	}
}
