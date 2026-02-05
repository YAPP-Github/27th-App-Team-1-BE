package com.yapp.ndgl.domain.travel.entity;

import com.yapp.ndgl.domain.common.entity.BaseEntity;
import com.yapp.ndgl.domain.travel.type.TravelProgramType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "travel_program")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelProgramEntity extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "profile_image", length = 1000)
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TravelProgramType type;

    @Builder
    public TravelProgramEntity(
        final String name,
        final String profileImage,
        final TravelProgramType type
    ) {
        this.name = name;
        this.profileImage = profileImage;
        this.type = type;
    }
}
