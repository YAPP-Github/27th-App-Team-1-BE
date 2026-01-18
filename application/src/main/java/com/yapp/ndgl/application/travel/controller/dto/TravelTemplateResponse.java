package com.yapp.ndgl.application.travel.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record TravelTemplateResponse(
    @Schema(description = "여행 템플릿 ID", example = "Unique_ID_문자열")
    String travelId,
    @Schema(description = "유튜버 이름", example = "유튜버_이름")
    String traveler,
    @Schema(description = "국가", example = "일본")
    String country,
    @Schema(description = "도시", example = "도쿄")
    String city,
    @Schema(description = "현지 날씨 및 복장 정보", example = "여름철 고온다습, 가벼운 옷차림 권장")
    String weatherInfo,
    @Schema(description = "문화적 주의사항 및 에티켓", example = "식당에서 팁 불필요, 조용히 식사하는 것이 예의")
    String cultureInfo,
    @Schema(description = "전반적인 음식 관련 특징", example = "라멘과 초밥이 유명하며, 현지 식당에서 현금 결제가 일반적")
    String foodInfo,
    @Schema(description = "썸네일 이미지 URL", example = "https://example.com/thumbnail.jpg")
    String thumbnail,
    @Schema(description = "유튜브 링크", example = "https://www.youtube.com/watch?v=example")
    String link,
    @Schema(description = "1인 기준 총 예산 (원)", example = "1000000")
    Integer budgetPerPerson,
    @Schema(description = "영상 요약", example = "도쿄 여행의 모든 것")
    String summary,
    @Schema(description = "제목", example = "도쿄 3박 4일 여행 가이드")
    String title,
    @Schema(description = "박 수", example = "3")
    Integer nights,
    @Schema(description = "일 수", example = "4")
    Integer days,
    @Schema(description = "여행 템플릿에 포함된 장소 목록")
    List<TravelTemplatePlaceResponse> places
) {
    public record TravelTemplatePlaceResponse(
        @Schema(description = "순서", example = "1")
        Integer sequence,
        @Schema(description = "일차", example = "1")
        Integer day,
        @Schema(description = "여행자 팁", example = "저녁 시간대 방문 추천")
        String travelerTip,
        @Schema(description = "장소 정보")
        PlaceResponse place
    ) {
    }

    public record PlaceResponse(
        @Schema(description = "장소 ID", example = "ChIJSc8jdZORQTURu6BMwxrKbGg")
        String placeId,
        @Schema(description = "주소", example = "123 Main St, Tokyo, Japan")
        String formattedAddress,
        @Schema(description = "위도", example = "35.6762")
        Double latitude,
        @Schema(description = "경도", example = "139.6503")
        Double longitude,
        @Schema(description = "평점", example = "4.5")
        Double rating,
        @Schema(description = "국내 전화번호", example = "03-1234-5678")
        String nationalPhoneNumber,
        @Schema(description = "국제 전화번호", example = "+81-3-1234-5678")
        String internationalPhoneNumber,
        @Schema(description = "웹사이트 URI", example = "https://example.com")
        String websiteUri,
        @Schema(description = "Google Maps URI", example = "https://maps.google.com/?cid=123456")
        String googleMapsUri,
        @Schema(description = "사용자 평점 수", example = "1234")
        Integer userRatingCount
    ) {
    }
}
