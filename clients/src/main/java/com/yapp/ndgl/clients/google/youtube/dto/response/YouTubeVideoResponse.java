package com.yapp.ndgl.clients.google.youtube.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * YouTube Data API videos.list 응답 모델.
 */
public record YouTubeVideoResponse(
	@JsonProperty("items") List<Item> items
) {

	public record Item(
		@JsonProperty("id") String id,
		@JsonProperty("snippet") Snippet snippet
	) {
	}

	public record Snippet(
		@JsonProperty("title") String title,
		@JsonProperty("channelId") String channelId,
		@JsonProperty("channelTitle") String channelTitle,
		@JsonProperty("thumbnails") Thumbnails thumbnails
	) {
	}

	public record Thumbnails(
		@JsonProperty("default") ThumbnailInfo defaultThumbnail,
		@JsonProperty("medium") ThumbnailInfo medium,
		@JsonProperty("high") ThumbnailInfo high,
		@JsonProperty("standard") ThumbnailInfo standard,
		@JsonProperty("maxres") ThumbnailInfo maxres
	) {
		/**
		 * 가장 높은 해상도의 썸네일 URL을 반환한다.
		 */
		public String bestThumbnailUrl() {
			if (maxres != null && maxres.url() != null) return maxres.url();
			if (standard != null && standard.url() != null) return standard.url();
			if (high != null && high.url() != null) return high.url();
			if (medium != null && medium.url() != null) return medium.url();
			if (defaultThumbnail != null && defaultThumbnail.url() != null) return defaultThumbnail.url();
			return null;
		}
	}

	public record ThumbnailInfo(
		@JsonProperty("url") String url,
		@JsonProperty("width") Integer width,
		@JsonProperty("height") Integer height
	) {
	}
}
