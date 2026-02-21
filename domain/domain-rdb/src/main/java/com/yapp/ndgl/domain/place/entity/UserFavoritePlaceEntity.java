package com.yapp.ndgl.domain.place.entity;

import com.yapp.ndgl.domain.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "user_favorite_places",
    indexes = {
        @Index(name = "idx_user_favorite_places_user_id", columnList = "user_id"),
        @Index(name = "idx_user_favorite_places_place_id", columnList = "place_id")
    },
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_user_favorite_place",
            columnNames = {"user_id", "place_id"}
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFavoritePlaceEntity extends BaseEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "place_id", nullable = false)
    private Long placeId;

    @Builder
    public UserFavoritePlaceEntity(final Long userId, final Long placeId) {
        this.userId = userId;
        this.placeId = placeId;
    }
}
