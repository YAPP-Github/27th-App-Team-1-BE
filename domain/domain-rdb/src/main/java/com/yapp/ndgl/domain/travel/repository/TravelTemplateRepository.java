package com.yapp.ndgl.domain.travel.repository;

import com.yapp.ndgl.domain.travel.entity.TravelTemplateEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TravelTemplateRepository extends JpaRepository<TravelTemplateEntity, Long>, TravelTemplateRepositoryCustom {

    @Modifying
    @Query("update TravelTemplateEntity t set t.viewCount = t.viewCount + 1 where t.id = :id")
    int incrementViewCount(@Param("id") Long id);

    Slice<TravelTemplateEntity> findAllByOrderByViewCountDesc(Pageable pageable);

    Slice<TravelTemplateEntity> findByTravelProgramIdOrderByViewCountDesc(Long travelProgramId, Pageable pageable);

    boolean existsByLink(String link);

}
