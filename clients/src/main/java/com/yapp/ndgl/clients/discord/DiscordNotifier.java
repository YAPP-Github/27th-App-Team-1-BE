package com.yapp.ndgl.clients.discord;

import org.springframework.stereotype.Component;

import com.yapp.ndgl.clients.discord.request.DiscordWebhookRequest;

import lombok.RequiredArgsConstructor;

/**
 * 알림 종류({@link DiscordNotification})를 Discord webhook 페이로드로 감싸 전송한다.
 */
@Component
@RequiredArgsConstructor
public class DiscordNotifier {

	private final DiscordWebhookClient discordWebhookClient;

	public void notify(final DiscordNotification notification) {
		DiscordWebhookRequest request = DiscordWebhookRequest.of(
			notification.createContent(),
			notification.createEmbeds()
		);
		discordWebhookClient.send(request);
	}
}
