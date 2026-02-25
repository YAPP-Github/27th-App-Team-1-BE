package com.yapp.ndgl.domain.place.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.yapp.ndgl.domain.place.entity.PlaceEntity;

public interface PlaceRepository extends JpaRepository<PlaceEntity, Long> {

	Optional<PlaceEntity> findByGooglePlaceId(final String googlePlaceId);

	boolean existsByGooglePlaceId(final String googlePlaceId);

	List<PlaceEntity> findByGooglePlaceIdIn(List<String> googlePlaceIds);

	@Modifying
	@Query("UPDATE PlaceEntity p SET p.nearbyPlacesJson = :nearbyPlacesJson WHERE p.googlePlaceId = :googlePlaceId")
	void updateNearbyPlacesJson(@Param("googlePlaceId") String googlePlaceId, @Param("nearbyPlacesJson") String nearbyPlacesJson);

}
