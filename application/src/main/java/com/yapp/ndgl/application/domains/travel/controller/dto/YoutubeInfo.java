package com.yapp.ndgl.application.domains.travel.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record YoutubeInfo(
    @Schema(description = "영상 제목", example = "도쿄 3박 4일 완벽 여행 가이드", requiredMode = Schema.RequiredMode.REQUIRED)
    String title,
    @Schema(description = "프로그램명 (유튜버명)", example = "빠니보틀", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,
    @Schema(description = "유튜버 프로필 이미지 URL", example = "https://example.com/thumbnail/panibottle.jpg", nullable = true)
    String profileImage,
    @Schema(description = "영상 썸네일 URL", example = "https://example.com/thumbnail/tokyo.jpg", nullable = true)
    String thumbnail,
    @Schema(description = "영상 링크", example = "https://www.youtube.com/watch?v=tokyo-travel", nullable = true)
    String link,
    @Schema(description = "영상 요약", example = "도쿄 3박 4일 여행의 모든 것. 유튜버가 직접 다녀온 코스로 구성된 완벽한 가이드.", requiredMode = Schema.RequiredMode.REQUIRED)
    String summary
) {

    public static YoutubeInfo of(
        final String title,
        final String name,
        final String profileImage,
        final String thumbnail,
        final String link,
        final String summary
    ) {
        return new YoutubeInfo(
            title,
            name,
            profileImage,
            thumbnail,
            link,
            summary
        );
    }
}
