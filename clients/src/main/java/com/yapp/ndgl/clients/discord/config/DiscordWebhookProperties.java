package com.yapp.ndgl.clients.discord.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "discord")
public record DiscordWebhookProperties(
	String webhookUrl
) {
}
