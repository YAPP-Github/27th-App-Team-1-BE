package com.yapp.ndgl.domain.travel.repository;

import com.yapp.ndgl.domain.travel.entity.TravelTemplatePlaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelTemplatePlaceRepository extends JpaRepository<TravelTemplatePlaceEntity, Long> {

    List<TravelTemplatePlaceEntity> findByTravelTemplateIdOrderByDayAscSequenceAsc(Long travelTemplateId);

}
