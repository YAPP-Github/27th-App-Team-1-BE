package com.yapp.ndgl.clients.google.places.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record PlaceTextSearchRequest(
	@JsonProperty("textQuery") String textQuery) {

	public static PlaceTextSearchRequest of(final String textQuery) {
		return PlaceTextSearchRequest.builder()
			.textQuery(textQuery)
			.build();
	}
}
