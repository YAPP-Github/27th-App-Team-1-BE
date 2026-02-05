package com.yapp.ndgl.domain.travel.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yapp.ndgl.domain.travel.entity.TravelTemplateEntity;

public interface TravelTemplateRepositoryCustom {

    List<TravelTemplateEntity> findRandomTemplates(String country, Pageable pageable);
}
