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

    public static TravelProgramEntity toEntity(final TravelProgram program) {
        return TravelProgramEntity.builder()
            .name(program.getName())
            .profileImage(program.getProfileImage())
            .type(program.getType())
            .build();
    }
}
