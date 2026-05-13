package com.yapp.ndgl.clients.discord.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yapp.ndgl.clients.discord.DiscordPayloadConstraints;
import com.yapp.ndgl.common.util.TextUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record DiscordWebhookRequest(
	String content,
	List<DiscordEmbed> embeds
) {
	public DiscordWebhookRequest {
		content = TextUtils.truncate(content, DiscordPayloadConstraints.CONTENT_MAX);
	}

	public static DiscordWebhookRequest of(final List<DiscordEmbed> embeds) {
		return new DiscordWebhookRequest(null, embeds);
	}

	public static DiscordWebhookRequest of(final String content) {
		return new DiscordWebhookRequest(content, null);
	}

	public static DiscordWebhookRequest of(final String content, final List<DiscordEmbed> embeds) {
		return new DiscordWebhookRequest(content, embeds);
	}
}
