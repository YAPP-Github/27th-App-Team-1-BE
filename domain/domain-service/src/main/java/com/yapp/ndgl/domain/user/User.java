package com.yapp.ndgl.domain.user;

import java.time.LocalDateTime;
import java.util.UUID;

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
    String uuid = UUID.randomUUID().toString();
    LocalDateTime now = LocalDateTime.now();

    return User.builder()
        .uuid(uuid)
        .fcmToken(fcmToken)
        .deviceModel(deviceModel)
        .deviceOs(deviceOs)
        .deviceOsVersion(deviceOsVersion)
        .appVersion(appVersion)
        .nickname(nickname)
        .createdAt(now)
        .updatedAt(now)
        .build();
  }
}
