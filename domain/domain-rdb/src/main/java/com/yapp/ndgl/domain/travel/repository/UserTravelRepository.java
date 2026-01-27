package com.yapp.ndgl.domain.travel.repository;

import com.yapp.ndgl.domain.travel.entity.UserTravelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTravelRepository extends JpaRepository<UserTravelEntity, Long> {
}
