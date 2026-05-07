package com.yapp.ndgl.clients.google.youtube;

import java.net.SocketTimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import com.yapp.ndgl.clients.google.youtube.dto.response.YouTubeChannelResponse;
import com.yapp.ndgl.clients.google.youtube.dto.response.YouTubeVideoResponse;
import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.YouTubeErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring RestClient로 YouTube Data API v3를 호출하는 클라이언트.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class YouTubeDataClient {

	@Value("${google.youtube.api-key}")
	private String apiKey;

	private static final String VIDEO_PART = "snippet";
	private static final String CHANNEL_PART = "snippet";

	/**
	 * YouTube 영상 URL에서 videoId를 추출하기 위한 패턴.
	 * 지원 형식: watch?v=, youtu.be/, embed/, shorts/
	 */
	private static final Pattern VIDEO_ID_PATTERN = Pattern.compile(
		"(?:youtube\\.com/(?:watch\\?v=|embed/|shorts/)|youtu\\.be/)([A-Za-z0-9_-]{10,30})"
	);

	private final RestClient youTubeDataRestClient;

	/**
	 * YouTube 영상 URL에서 videoId를 추출한다.
	 */
	public String extractVideoId(final String videoUrl) {
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

	/**
	 * YouTube Data API videos.list를 호출하여 영상 정보를 조회한다.
	 */
	public YouTubeVideoResponse readVideoInfo(final String videoId) {
		try {
			if (!StringUtils.hasText(videoId)) {
				throw new GlobalException(YouTubeErrorCode.INVALID_VIDEO_URL);
			}

			log.info("YouTube Data API videos.list 호출: videoId={}", videoId);

			YouTubeVideoResponse response = youTubeDataRestClient.get()
				.uri(uriBuilder -> uriBuilder
					.path("/videos")
					.queryParam("part", VIDEO_PART)
					.queryParam("id", videoId)
					.queryParam("key", apiKey)
					.build())
				.retrieve()
				.onStatus(HttpStatusCode::isError, (req, res) -> {
					log.error("YouTube Data API videos.list 응답 오류 (status={})", res.getStatusCode());
					throw new GlobalException(YouTubeErrorCode.API_CALL_FAILED);
				})
				.body(YouTubeVideoResponse.class);

			validateVideoResponse(response, videoId);
			log.debug("YouTube Data API videos.list 호출 성공: videoId={}", videoId);
			return response;

		} catch (ResourceAccessException e) {
			log.error("YouTube Data API videos.list 요청 실패: {}", e.getMessage(), e);
			if (e.getCause() instanceof SocketTimeoutException) {
				throw new GlobalException(YouTubeErrorCode.API_TIMEOUT);
			}
			throw new GlobalException(YouTubeErrorCode.API_CALL_FAILED);
		}
	}

	/**
	 * YouTube Data API channels.list를 호출하여 채널 프로필 이미지를 조회한다.
	 */
	public YouTubeChannelResponse readChannelInfo(final String channelId) {
		try {
			if (!StringUtils.hasText(channelId)) {
				throw new GlobalException(YouTubeErrorCode.CHANNEL_NOT_FOUND);
			}

			log.info("YouTube Data API channels.list 호출: channelId={}", channelId);

			YouTubeChannelResponse response = youTubeDataRestClient.get()
				.uri(uriBuilder -> uriBuilder
					.path("/channels")
					.queryParam("part", CHANNEL_PART)
					.queryParam("id", channelId)
					.queryParam("key", apiKey)
					.build())
				.retrieve()
				.onStatus(HttpStatusCode::isError, (req, res) -> {
					log.error("YouTube Data API channels.list 응답 오류 (status={})", res.getStatusCode());
					throw new GlobalException(YouTubeErrorCode.API_CALL_FAILED);
				})
				.body(YouTubeChannelResponse.class);

			validateChannelResponse(response, channelId);
			log.debug("YouTube Data API channels.list 호출 성공: channelId={}", channelId);
			return response;

		} catch (ResourceAccessException e) {
			log.error("YouTube Data API channels.list 요청 실패: {}", e.getMessage(), e);
			if (e.getCause() instanceof SocketTimeoutException) {
				throw new GlobalException(YouTubeErrorCode.API_TIMEOUT);
			}
			throw new GlobalException(YouTubeErrorCode.API_CALL_FAILED);
		}
	}

	private void validateVideoResponse(final YouTubeVideoResponse response, final String videoId) {
		if (response == null || response.items() == null || response.items().isEmpty()) {
			log.error("YouTube Data API videos.list 응답이 비어있습니다: videoId={}", videoId);
			throw new GlobalException(YouTubeErrorCode.VIDEO_NOT_FOUND);
		}
	}

	private void validateChannelResponse(final YouTubeChannelResponse response, final String channelId) {
		if (response == null || response.items() == null || response.items().isEmpty()) {
			log.error("YouTube Data API channels.list 응답이 비어있습니다: channelId={}", channelId);
			throw new GlobalException(YouTubeErrorCode.CHANNEL_NOT_FOUND);
		}
	}
}
