package com.yapp.ndgl.domain.travel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yapp.ndgl.domain.travel.entity.TravelProgramEntity;

public interface TravelProgramRepository extends JpaRepository<TravelProgramEntity, Long> {

    Optional<TravelProgramEntity> findByName(String name);
}
