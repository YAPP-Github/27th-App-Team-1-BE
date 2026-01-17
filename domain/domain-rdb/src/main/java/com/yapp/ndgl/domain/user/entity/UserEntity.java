package com.yapp.ndgl.domain.user.entity;

import com.yapp.ndgl.domain.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

  @Column(nullable = false, unique = true, length = 36)
  private String uuid;

  @Column(nullable = false, length = 500)
  private String fcmToken;

  @Column(length = 100)
  private String deviceModel;

  @Column(length = 50)
  private String deviceOs;

  @Column(length = 100)
  private String deviceOsVersion;

  @Column(length = 50)
  private String appVersion;

  @Builder
  public UserEntity(
      final String uuid,
      final String fcmToken,
      final String deviceModel,
      final String deviceOs,
      final String deviceOsVersion,
      final String appVersion
  ) {
    this.uuid = uuid;
    this.fcmToken = fcmToken;
    this.deviceModel = deviceModel;
    this.deviceOs = deviceOs;
    this.deviceOsVersion = deviceOsVersion;
    this.appVersion = appVersion;
  }
}
