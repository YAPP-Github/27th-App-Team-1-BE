package com.yapp.ndgl.domain.travel;

import com.yapp.ndgl.domain.travel.type.TravelProgramType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TravelProgram {

    private Long id;
    private String name;
    private String profileImage;
    private TravelProgramType type;

    public static TravelProgram create(
        final String name,
        final String profileImage,
        final TravelProgramType type
    ) {
        return TravelProgram.builder()
            .name(name)
            .profileImage(profileImage)
            .type(type)
            .build();
    }
}
