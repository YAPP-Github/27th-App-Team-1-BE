package com.yapp.ndgl.domain.user.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

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
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();
  }
}
