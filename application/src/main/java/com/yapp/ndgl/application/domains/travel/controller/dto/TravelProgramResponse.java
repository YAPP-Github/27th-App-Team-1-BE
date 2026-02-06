package com.yapp.ndgl.application.domains.travel.controller.dto;

import com.yapp.ndgl.domain.travel.TravelProgram;
import com.yapp.ndgl.domain.travel.type.TravelProgramType;

import io.swagger.v3.oas.annotations.media.Schema;

public record TravelProgramResponse(
    @Schema(description = "프로그램 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    Long id,
    @Schema(description = "프로그램명", example = "빠니보틀", requiredMode = Schema.RequiredMode.REQUIRED)
    String name,
    @Schema(description = "프로그램 프로필 이미지 URL", example = "https://example.com/thumbnail/panibottle.jpg", nullable = true)
    String profileImage,
    @Schema(description = "프로그램 타입", example = "YOUTUBE", requiredMode = Schema.RequiredMode.REQUIRED)
    TravelProgramType type
) {

    public static TravelProgramResponse from(final TravelProgram program) {
        return new TravelProgramResponse(
            program.getId(),
            program.getName(),
            program.getProfileImage(),
            program.getType()
        );
    }

}
