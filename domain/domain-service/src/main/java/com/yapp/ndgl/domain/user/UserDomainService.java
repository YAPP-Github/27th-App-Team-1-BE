package com.yapp.ndgl.domain.user;

import org.springframework.stereotype.Service;

import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.UserErrorCode;
import com.yapp.ndgl.domain.user.entity.UserEntity;
import com.yapp.ndgl.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;

    public User createUser(
        final String fcmToken,
        final String deviceModel,
        final String deviceOs,
        final String deviceOsVersion,
        final String appVersion) {
        String nickname = UserNicknameGenerator.generate();

        User user = User.create(
            fcmToken,
            deviceModel,
            deviceOs,
            deviceOsVersion,
            appVersion,
            nickname
        );

        UserEntity savedUserEntity = userRepository.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(savedUserEntity);
    }

    public User findByUuid(final String uuid) {
        return userRepository.findByUuid(uuid)
            .map(UserMapper::toDomain)
            .orElseThrow(() -> new GlobalException(UserErrorCode.NOT_FOUND_USER));
    }
}
