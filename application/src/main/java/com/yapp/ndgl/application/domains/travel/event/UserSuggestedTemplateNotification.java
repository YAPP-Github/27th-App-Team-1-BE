package com.yapp.ndgl.application.domains.travel.event;

import java.time.Instant;
import java.util.List;

import org.springframework.util.StringUtils;

import com.yapp.ndgl.clients.discord.DiscordChannel;
import com.yapp.ndgl.clients.discord.DiscordNotification;
import com.yapp.ndgl.clients.discord.DiscordPayloadConstraints;
import com.yapp.ndgl.clients.discord.request.DiscordEmbed;
import com.yapp.ndgl.clients.discord.request.DiscordEmbed.Field;
import com.yapp.ndgl.clients.discord.request.DiscordEmbed.Image;
import com.yapp.ndgl.common.util.TextUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UserSuggestedTemplateNotification implements DiscordNotification {

	private static final int EMBED_COLOR = 5763719;
	private static final String CONTENT_HEADER = "## 🎬 새로운 영상이 요청되었어요. 지금 확인해보세요!";
	private static final String EMBED_TITLE = "영상 보기";
	private static final String EMPTY_PLACEHOLDER = "-";
	private static final String CATEGORY_DELIMITER = ", ";

	private final UserSuggestedTemplateCreatedEvent event;

	public static UserSuggestedTemplateNotification from(final UserSuggestedTemplateCreatedEvent event) {
		return new UserSuggestedTemplateNotification(event);
	}

	@Override
	public DiscordChannel channel() {
		return DiscordChannel.USER_SUGGESTED_TEMPLATE;
	}

	@Override
	public String createContent() {
		return CONTENT_HEADER;
	}

	@Override
	public List<DiscordEmbed> createEmbeds() {

		List<Field> fields = List.of(
			Field.of("카테고리", TextUtils.joinOrDefault(event.category(), CATEGORY_DELIMITER, EMPTY_PLACEHOLDER), true),
			Field.of("템플릿 ID", "#" + event.templateId(), true),
			Field.of("제안자", maskUuid(event.suggesterUuid()), false)
		);

		String description = TextUtils.defaultIfBlank(event.recommendReason(), null);
		Image image = StringUtils.hasText(event.videoId()) ?
			Image.of(DiscordPayloadConstraints.YOUTUBE_THUMBNAIL_URL_FORMAT.formatted(event.videoId())) : null;

		DiscordEmbed embed = DiscordEmbed.builder()
			.title(EMBED_TITLE)
			.url(event.videoLink())
			.color(EMBED_COLOR)
			.timestamp(Instant.now().toString())
			.description(description)
			.image(image)
			.fields(fields)
			.build();

		return List.of(embed);
	}

	private static String maskUuid(final String uuid) {
		if (!StringUtils.hasText(uuid)) {
			return EMPTY_PLACEHOLDER;
		}
		if (uuid.length() <= 8) {
			return uuid;
		}
		return uuid.substring(0, 8) + "…";
	}
}
