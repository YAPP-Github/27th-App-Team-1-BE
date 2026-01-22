package com.yapp.ndgl.domain.place.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yapp.ndgl.domain.place.entity.PlacePhotoEntity;

public interface PlacePhotoRepository extends JpaRepository<PlacePhotoEntity, Long> {

	List<PlacePhotoEntity> findByGooglePlaceId(String googlePlaceId);

	Optional<PlacePhotoEntity> findByGooglePlaceIdAndPhotoName(String googlePlaceId, String photoName);

	boolean existsByGooglePlaceIdAndPhotoName(String googlePlaceId, String photoName);

	boolean existsByPhotoName(String photoName);

	List<PlacePhotoEntity> findByPhotoNameIn(List<String> photoNames);
}
