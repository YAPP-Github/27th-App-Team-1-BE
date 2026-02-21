package com.yapp.ndgl.domain.place.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yapp.ndgl.domain.place.entity.PlaceEntity;

public interface UserFavoritePlaceRepositoryCustom {

    List<PlaceEntity> findFavoritePlacesByUserId(Long userId, Pageable pageable);
}
