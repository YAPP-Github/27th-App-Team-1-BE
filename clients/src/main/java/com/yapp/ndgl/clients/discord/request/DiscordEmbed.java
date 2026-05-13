package com.yapp.ndgl.clients.discord.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yapp.ndgl.clients.discord.DiscordPayloadConstraints;
import com.yapp.ndgl.common.util.TextUtils;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DiscordEmbed(
	String title,
	String description,
	String url,
	Integer color,
	String timestamp,
	Image image,
	Thumbnail thumbnail,
	List<Field> fields
) {
	public DiscordEmbed {
		title = TextUtils.truncate(title, DiscordPayloadConstraints.EMBED_TITLE_MAX);
		description = TextUtils.truncate(description, DiscordPayloadConstraints.EMBED_DESCRIPTION_MAX);

		if (fields != null && fields.size() > DiscordPayloadConstraints.FIELDS_MAX_COUNT) {
			fields = List.copyOf(fields.subList(0, DiscordPayloadConstraints.FIELDS_MAX_COUNT));
		}
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record Field(
		String name,
		String value,
		Boolean inline
	) {
		public Field {
			name = TextUtils.truncate(name, DiscordPayloadConstraints.FIELD_NAME_MAX);
			value = TextUtils.truncate(value, DiscordPayloadConstraints.FIELD_VALUE_MAX);
		}

		public static Field of(final String name, final String value, final boolean inline) {
			return new Field(name, value, inline);
		}
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record Image(String url) {
		public static Image of(final String url) {
			return url == null ? null : new Image(url);
		}
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public record Thumbnail(String url) {
		public static Thumbnail of(final String url) {
			return url == null ? null : new Thumbnail(url);
		}
	}
}
