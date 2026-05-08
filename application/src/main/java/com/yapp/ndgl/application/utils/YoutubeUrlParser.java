package com.yapp.ndgl.application.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.YouTubeErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YoutubeUrlParser {

	/**
	 * YouTube 영상 URL에서 videoId를 추출하기 위한 패턴.
	 * 지원 형식: watch?v=, youtu.be/, embed/, shorts/
	 */
	private static final Pattern VIDEO_ID_PATTERN = Pattern.compile(
		"(?:youtube\\.com/(?:watch\\?v=|embed/|shorts/)|youtu\\.be/)([A-Za-z0-9_-]{10,30})"
	);

	/**
	 * YouTube 영상 URL에서 videoId를 추출한다.
	 */
	public static String extractVideoId(final String videoUrl) {
		if (!StringUtils.hasText(videoUrl)) {
			log.error("YouTube 영상 URL이 비어있습니다");
			throw new GlobalException(YouTubeErrorCode.INVALID_VIDEO_URL);
		}

		Matcher matcher = VIDEO_ID_PATTERN.matcher(videoUrl);
		if (!matcher.find()) {
			log.error("YouTube 영상 URL에서 videoId를 추출할 수 없습니다: url={}", videoUrl);
			throw new GlobalException(YouTubeErrorCode.INVALID_VIDEO_URL);
		}

		String videoId = matcher.group(1);
		log.debug("YouTube videoId 추출 성공: videoId={}", videoId);
		return videoId;
	}
}
