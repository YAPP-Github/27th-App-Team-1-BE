package com.yapp.ndgl.clients.discord;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import com.yapp.ndgl.clients.discord.config.DiscordWebhookProperties;
import com.yapp.ndgl.clients.discord.request.DiscordWebhookRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordWebhookClient {

	private final RestClient discordWebhookRestClient;
	private final DiscordWebhookProperties properties;

	public void send(final DiscordWebhookRequest request) {
		if (!StringUtils.hasText(properties.webhookUrl())) {
			log.warn("Discord webhook URL이 설정되지 않아 알림을 스킵합니다.");
			return;
		}

		try {
			discordWebhookRestClient.post()
				.uri(properties.webhookUrl())
				.contentType(MediaType.APPLICATION_JSON)
				.body(request)
				.retrieve()
				.toBodilessEntity();
			log.debug("Discord 알림 전송 성공");
		} catch (Exception e) {
			log.error("Discord 알림 전송에 실패했습니다.", e);
		}
	}
}
