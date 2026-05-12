package com.yapp.ndgl.clients.discord;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.yapp.ndgl.clients.discord.config.DiscordWebhookProperties;
import com.yapp.ndgl.clients.discord.request.DiscordWebhookRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 알림 종류({@link DiscordNotification})를 Discord webhook 페이로드로 감싸 전송한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordNotifier {

	private final DiscordWebhookClient discordWebhookClient;
	private final DiscordWebhookProperties properties;

	public void notify(final DiscordNotification notification) {
		String webhookUrl = properties.webhookUrl(notification.channel());
		if (!StringUtils.hasText(webhookUrl)) {
			log.warn("Discord webhook URL이 설정되지 않아 알림을 스킵합니다. channel={}", notification.channel());
			return;
		}

		DiscordWebhookRequest request = DiscordWebhookRequest.of(
			notification.createContent(),
			notification.createEmbeds()
		);
		discordWebhookClient.send(webhookUrl, request);
	}
}
