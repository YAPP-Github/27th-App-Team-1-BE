package com.yapp.ndgl.domain.travel.repository;

import java.util.List;
import java.util.Optional;

import com.yapp.ndgl.domain.travel.entity.UserTravelPlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTravelPlaceRepository extends JpaRepository<UserTravelPlaceEntity, Long> {
    Optional<UserTravelPlaceEntity> findTopByUserTravelIdOrderByDayAscSequenceAsc(Long userTravelId);

    List<UserTravelPlaceEntity> findByUserTravelIdAndDayOrderBySequenceAsc(Long userTravelId, Integer day);

    List<UserTravelPlaceEntity> findByIdIn(List<Long> ids);

    void deleteByUserTravelId(Long userTravelId);
}
