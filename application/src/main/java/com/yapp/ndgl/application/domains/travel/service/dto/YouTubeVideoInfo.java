package com.yapp.ndgl.application.domains.travel.service.dto;

/**
 * YouTube Data API에서 수집한 영상 및 채널 정보를 담는 내부 전달 객체.
 * Phase 1(외부 API 호출)에서 생성되어 Phase 2(DB 저장)로 전달된다.
 */
public record YouTubeVideoInfo(
	String videoTitle,
	String thumbnailUrl,
	String channelName,
	String channelProfileImage
) {
}
