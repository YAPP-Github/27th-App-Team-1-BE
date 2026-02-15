package com.yapp.ndgl.clients.google.places.dto.response;

import java.util.List;

public record GooglePlaceTextSearchResponse(
	List<PlaceResult> places) {

	public record PlaceResult(
		String id,
		DisplayName displayName) {

		public record DisplayName(String text, String languageCode) {
		}
	}
}
