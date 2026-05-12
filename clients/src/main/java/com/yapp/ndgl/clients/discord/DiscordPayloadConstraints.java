package com.yapp.ndgl.clients.discord;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Discord 페이로드의 길이/개수 제약 상수.
 * <p>Discord API 문서를 기준으로 한 메시지/embed의 한도를 한 곳에서 관리한다.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DiscordPayloadConstraints {

	public static final int CONTENT_MAX = 2000;
	public static final int EMBED_TITLE_MAX = 256;
	public static final int EMBED_DESCRIPTION_MAX = 4096;
	public static final int FIELD_NAME_MAX = 256;
	public static final int FIELD_VALUE_MAX = 1024;
	public static final int FIELDS_MAX_COUNT = 25;

	public static final String YOUTUBE_THUMBNAIL_URL_FORMAT = "https://img.youtube.com/vi/%s/hqdefault.jpg";
}
