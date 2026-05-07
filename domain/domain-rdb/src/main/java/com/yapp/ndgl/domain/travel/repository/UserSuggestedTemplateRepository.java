package com.yapp.ndgl.domain.travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yapp.ndgl.domain.travel.entity.UserSuggestedTemplateEntity;

public interface UserSuggestedTemplateRepository
    extends JpaRepository<UserSuggestedTemplateEntity, Long> {

    boolean existsByVideoId(String videoId);
}
