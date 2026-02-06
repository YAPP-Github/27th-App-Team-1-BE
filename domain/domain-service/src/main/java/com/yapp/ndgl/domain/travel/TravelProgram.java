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
}
