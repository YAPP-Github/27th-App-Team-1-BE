package com.yapp.ndgl.clients.discord.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.yapp.ndgl.clients.discord.DiscordChannel;

@ConfigurationProperties(prefix = "discord")
public record DiscordWebhookProperties(
	Map<String, String> webhooks
) {
	public String webhookUrl(final DiscordChannel channel) {
		if (webhooks == null) {
			return null;
		}
		return webhooks.get(channel.getKey());
	}
}
