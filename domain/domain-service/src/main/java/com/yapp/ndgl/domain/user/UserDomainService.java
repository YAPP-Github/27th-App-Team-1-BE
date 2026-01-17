package com.yapp.ndgl.domain.user;

import org.springframework.stereotype.Service;

import com.yapp.ndgl.domain.user.entity.UserEntity;
import com.yapp.ndgl.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDomainService {

  private final UserRepository userRepository;

  public String createUser(
      final String fcmToken,
      final String deviceModel,
      final String deviceOs,
      final String deviceOsVersion,
      final String appVersion) {
    User user = User.create(
        fcmToken,
        deviceModel,
        deviceOs,
        deviceOsVersion,
        appVersion
    );

    UserEntity savedUserEntity = userRepository.save(UserMapper.toEntity(user));
    User savedUser = UserMapper.toDomain(savedUserEntity);
    return savedUser.getUuid();
  }
}
