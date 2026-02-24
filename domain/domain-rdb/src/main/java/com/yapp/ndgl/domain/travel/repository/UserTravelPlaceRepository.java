package com.yapp.ndgl.domain.travel.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.yapp.ndgl.domain.travel.entity.UserTravelPlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTravelPlaceRepository extends JpaRepository<UserTravelPlaceEntity, Long> {
    Optional<UserTravelPlaceEntity> findTopByUserTravelIdOrderByDayAscSequenceAsc(Long userTravelId);

    @Query("""
        SELECT utp
        FROM UserTravelPlaceEntity utp
        WHERE utp.userTravelId = :userTravelId
          AND utp.day = :day
          AND (utp.startTime > :startTime OR utp.startTime IS NULL)
        ORDER BY
          CASE WHEN utp.startTime IS NULL THEN 1 ELSE 0 END,
          utp.startTime ASC,
          utp.sequence ASC
        """)
    Optional<UserTravelPlaceEntity> findTopByUserTravelIdAndDayAndStartTimeGreaterThanOrderByStartTimeAscSequenceAsc(
        @Param("userTravelId") Long userTravelId,
        @Param("day") Integer day,
        @Param("startTime") LocalTime startTime
    );

    List<UserTravelPlaceEntity> findByUserTravelIdAndDayOrderBySequenceAsc(Long userTravelId, Integer day);

    List<UserTravelPlaceEntity> findByIdIn(List<Long> ids);

    Optional<UserTravelPlaceEntity> findByIdAndUserTravelId(Long id, Long userTravelId);

    boolean existsByUserTravelIdAndDayAndSequence(Long userTravelId, Integer day, Integer sequence);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM UserTravelPlaceEntity utp WHERE utp.userTravelId = :userTravelId")
    void deleteByUserTravelId(@Param("userTravelId") Long userTravelId);
}
