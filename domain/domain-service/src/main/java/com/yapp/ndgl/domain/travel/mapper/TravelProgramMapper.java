package com.yapp.ndgl.domain.travel.mapper;

import com.yapp.ndgl.domain.travel.TravelProgram;
import com.yapp.ndgl.domain.travel.entity.TravelProgramEntity;

public class TravelProgramMapper {

    public static TravelProgram toDomain(final TravelProgramEntity entity) {
        if (entity == null) {
            return null;
        }

        return TravelProgram.builder()
            .id(entity.getId())
            .name(entity.getName())
            .profileImage(entity.getProfileImage())
            .type(entity.getType())
            .build();
    }
}
