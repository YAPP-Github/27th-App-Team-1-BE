package com.yapp.ndgl.application.domains.travel.controller.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.yapp.ndgl.application.domains.place.controller.response.NearbyPlaceInfo;
import com.yapp.ndgl.domain.place.Place;
import com.yapp.ndgl.common.type.PlaceCategory;

import io.swagger.v3.oas.annotations.media.Schema;

public record PlaceInfo(
    @Schema(description = "Google Places 장소 ID", example = "ChIJSc8jdZORQTURu6BMwxrKbGg", requiredMode = Schema.RequiredMode.REQUIRED)
    String googlePlaceId,
    @Schema(description = "장소 썸네일", example = "https://lh3.googleusercontent.com/place-photos/AEkURDym40I4XyqXUosRz8bTu9aPvDUklxkfM79KCa03C0SQTnDaTu_RXXiWQjCRZ3-yK4dTbzoySqMrucj1ubPQNUZ5yKseTRfmaME5C--5jLYB0rU-MLXqUabNEk3myTWywzIuEHcKz_I-H4Xtdg=s4800-w4800-h3600", nullable = true)
    String thumbnail,
    @Schema(description = "위도", example = "35.6762", requiredMode = Schema.RequiredMode.REQUIRED)
    Double latitude,
    @Schema(description = "경도", example = "139.6503", requiredMode = Schema.RequiredMode.REQUIRED)
    Double longitude,
    @Schema(description = "장소 이름", example = "도쿄타워", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,
    @Schema(description = "오늘의 영업시간 정보 (null인 경우 영업시간 정보 없음)", example = "09:00~23:00", nullable = true)
    String regularOpeningHours,
    @Schema(description = "Google Maps URI", example = "https://maps.google.com/?cid=14776686710302251978", requiredMode = Schema.RequiredMode.REQUIRED)
    String googleMapsUri,
    @Schema(description = "장소 카테고리", example = "TRANSPORT", requiredMode = Schema.RequiredMode.REQUIRED,
        allowableValues = {"AIRPORT", "TRANSPORT", "ATTRACTION", "RESTAURANT", "CAFE", "ACCOMMODATION"})
    PlaceCategory category,
    @Schema(description = "인근 장소 목록", nullable = true)
    List<NearbyPlaceInfo> nearbyPlaces
) {

    public static PlaceInfo from(final Place place, final Map<String, Place> nearbyPlaceMap) {
        if (place == null) {
            return null;
        }

        String todayOpeningHours = parseTodayOpeningHours(place.getRegularOpeningHours());
        List<NearbyPlaceInfo> nearbyPlaces = resolveNearbyPlaces(place.getNearbyPlaces(), nearbyPlaceMap);

        return new PlaceInfo(
            place.getGooglePlaceId(),
            place.getThumbnail(),
            place.getLatitude(),
            place.getLongitude(),
            place.getName(),
            todayOpeningHours,
            place.getGoogleMapsUri(),
            place.getCategory(),
            nearbyPlaces
        );
    }

    private static String parseTodayOpeningHours(final List<String> weekdayDescriptions) {
        if (weekdayDescriptions == null || weekdayDescriptions.isEmpty()) {
            return null;
        }

        int todayIndex = LocalDate.now().getDayOfWeek().getValue() - 1;
        if (todayIndex < 0 || todayIndex >= weekdayDescriptions.size()) {
            return null;
        }

        String todayDescription = weekdayDescriptions.get(todayIndex);
        int colonIndex = todayDescription.indexOf(':');
        if (colonIndex < 0) {
            return todayDescription;
        }

        String openingHours = todayDescription.substring(colonIndex + 1).trim();
        return openingHours.isEmpty() ? null : openingHours;
    }

    private static List<NearbyPlaceInfo> resolveNearbyPlaces(
        final List<String> nearbyIds,
        final Map<String, Place> nearbyPlaceMap
    ) {
        if (nearbyIds == null || nearbyIds.isEmpty() || nearbyPlaceMap == null) {
            return null;
        }
        List<NearbyPlaceInfo> result = nearbyIds.stream()
            .filter(nearbyPlaceMap::containsKey)
            .map(id -> NearbyPlaceInfo.from(nearbyPlaceMap.get(id)))
            .toList();
        return result.isEmpty() ? null : result;
    }
}
