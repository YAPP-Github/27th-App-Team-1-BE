package com.yapp.ndgl.domain.place.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yapp.ndgl.domain.place.entity.PlacePhotoEntity;

public interface PlacePhotoRepository extends JpaRepository<PlacePhotoEntity, Long> {

	List<PlacePhotoEntity> findByPlaceId(String placeId);

	Optional<PlacePhotoEntity> findByPlaceIdAndPhotoName(String placeId, String photoName);

	boolean existsByPlaceIdAndPhotoName(String placeId, String photoName);
}
