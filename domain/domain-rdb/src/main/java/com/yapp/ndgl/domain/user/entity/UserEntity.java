package com.yapp.ndgl.domain.user.entity;

import com.yapp.ndgl.common.type.SocialProvider;
import com.yapp.ndgl.domain.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "users",
    uniqueConstraints = @UniqueConstraint(columnNames = {"provider", "provider_id"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true, length = 36)
    private String uuid;

    @Column(length = 500)
    private String fcmToken;

    @Column(length = 100)
    private String deviceModel;

    @Column(length = 50)
    private String deviceOs;

    @Column(length = 100)
    private String deviceOsVersion;

    @Column(length = 50)
    private String appVersion;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SocialProvider provider;

    @Column(name = "provider_id", length = 100)
    private String providerId;

    @Column(length = 200)
    private String email;

    @Builder
    public UserEntity(
        final String uuid,
        final String fcmToken,
        final String deviceModel,
        final String deviceOs,
        final String deviceOsVersion,
        final String appVersion,
        final String nickname,
        final SocialProvider provider,
        final String providerId,
        final String email
    ) {
        this.uuid = uuid;
        this.fcmToken = fcmToken;
        this.deviceModel = deviceModel;
        this.deviceOs = deviceOs;
        this.deviceOsVersion = deviceOsVersion;
        this.appVersion = appVersion;
        this.nickname = nickname;
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
    }
}
