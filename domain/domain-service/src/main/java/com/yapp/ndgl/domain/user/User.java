package com.yapp.ndgl.domain.user;

import java.time.LocalDateTime;
import java.util.UUID;

import com.yapp.ndgl.common.type.SocialProvider;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {

    private final Long id;
    private final String uuid;
    private final String fcmToken;
    private final String deviceModel;
    private final String deviceOs;
    private final String deviceOsVersion;
    private final String appVersion;
    private final String nickname;
    private final SocialProvider provider;
    private final String providerId;
    private final String email;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static User create(
        final String fcmToken,
        final String deviceModel,
        final String deviceOs,
        final String deviceOsVersion,
        final String appVersion,
        final String nickname
    ) {
        return User.builder()
            .uuid(UUID.randomUUID().toString())
            .fcmToken(fcmToken)
            .deviceModel(deviceModel)
            .deviceOs(deviceOs)
            .deviceOsVersion(deviceOsVersion)
            .appVersion(appVersion)
            .nickname(nickname)
            .build();
    }

    public static User createSocialUser(
        final SocialProvider provider,
        final String providerId,
        final String email,
        final String nickname
    ) {
        return User.builder()
            .uuid(UUID.randomUUID().toString())
            .provider(provider)
            .providerId(providerId)
            .email(email)
            .nickname(nickname)
            .build();
    }
}
