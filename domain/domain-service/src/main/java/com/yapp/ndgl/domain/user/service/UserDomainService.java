package com.yapp.ndgl.domain.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yapp.ndgl.common.exception.GlobalException;
import com.yapp.ndgl.common.exception.UserErrorCode;
import com.yapp.ndgl.common.type.SocialProvider;
import com.yapp.ndgl.domain.user.User;
import com.yapp.ndgl.domain.user.UserNicknameGenerator;
import com.yapp.ndgl.domain.user.entity.UserEntity;
import com.yapp.ndgl.domain.user.mapper.UserMapper;
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
        User user = User.create(fcmToken, deviceModel, deviceOs, deviceOsVersion, appVersion, nickname);
        UserEntity savedUserEntity = userRepository.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(savedUserEntity);
    }

    @Transactional
    public User createSocialUser(
        final SocialProvider provider,
        final String providerId,
        final String email) {
        String nickname = UserNicknameGenerator.generate();
        User user = User.createSocialUser(provider, providerId, email, nickname);
        UserEntity savedEntity = userRepository.save(UserMapper.toEntity(user));
        return UserMapper.toDomain(savedEntity);
    }

    @Transactional(readOnly = true)
    public User findByUuid(final String uuid) {
        return userRepository.findByUuid(uuid)
            .map(UserMapper::toDomain)
            .orElseThrow(() -> new GlobalException(UserErrorCode.NOT_FOUND_USER));
    }

    @Transactional(readOnly = true)
    public Optional<User> findByProviderAndProviderId(
        final SocialProvider provider,
        final String providerId) {
        return userRepository.findByProviderAndProviderId(provider, providerId)
            .map(UserMapper::toDomain);
    }
}
