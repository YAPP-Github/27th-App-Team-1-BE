package com.yapp.ndgl.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yapp.ndgl.domain.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByUuid(String uuid);

}
