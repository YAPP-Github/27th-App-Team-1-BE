package com.yapp.ndgl.domain.travel.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.yapp.ndgl.domain.travel.entity.UserTravelPlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTravelPlaceRepository extends JpaRepository<UserTravelPlaceEntity, Long> {
    Optional<UserTravelPlaceEntity> findTopByUserTravelIdOrderByDayAscSequenceAsc(Long userTravelId);

    Optional<UserTravelPlaceEntity> findTopByUserTravelIdAndDayAndStartTimeGreaterThanOrderByStartTimeAsc(
        Long userTravelId, Integer day, LocalTime startTime);

    List<UserTravelPlaceEntity> findByUserTravelIdAndDayOrderBySequenceAsc(Long userTravelId, Integer day);

    List<UserTravelPlaceEntity> findByIdIn(List<Long> ids);

    void deleteByUserTravelId(Long userTravelId);
}
