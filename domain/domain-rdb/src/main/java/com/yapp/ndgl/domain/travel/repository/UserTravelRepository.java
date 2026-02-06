package com.yapp.ndgl.domain.travel.repository;

import java.time.LocalDate;
import java.util.Optional;

import com.yapp.ndgl.domain.travel.entity.UserTravelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTravelRepository extends JpaRepository<UserTravelEntity, Long> {
    Optional<UserTravelEntity> findTopByUserIdAndStartDateGreaterThanEqualOrderByStartDateAsc(
        Long userId, LocalDate startDate);
}
