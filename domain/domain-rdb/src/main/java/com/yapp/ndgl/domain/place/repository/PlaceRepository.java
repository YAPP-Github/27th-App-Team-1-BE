package com.yapp.ndgl.domain.place.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yapp.ndgl.domain.place.entity.PlaceEntity;

public interface PlaceRepository extends JpaRepository<PlaceEntity, Long> {

	Optional<PlaceEntity> findByPlaceId(String placeId);

}
