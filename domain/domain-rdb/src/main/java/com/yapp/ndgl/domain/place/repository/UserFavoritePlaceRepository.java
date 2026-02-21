package com.yapp.ndgl.domain.place.repository;

import com.yapp.ndgl.domain.place.entity.UserFavoritePlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoritePlaceRepository extends JpaRepository<UserFavoritePlaceEntity, Long> {

    boolean existsByUserIdAndPlaceId(Long userId, Long placeId);

    void deleteByUserIdAndPlaceId(Long userId, Long placeId);
}
