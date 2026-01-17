package com.yapp.ndgl.domain.user;

import com.yapp.ndgl.domain.user.entity.UserEntity;

public class UserMapper {

	public static UserEntity toEntity(final User user) {
		return UserEntity.builder()
			.uuid(user.getUuid())
			.fcmToken(user.getFcmToken())
			.deviceModel(user.getDeviceModel())
			.deviceOs(user.getDeviceOs())
			.deviceOsVersion(user.getDeviceOsVersion())
			.appVersion(user.getAppVersion())
			.build();
	}

	public static User toDomain(final UserEntity entity) {
		return User.builder()
			.id(entity.getId())
			.uuid(entity.getUuid())
			.fcmToken(entity.getFcmToken())
			.deviceModel(entity.getDeviceModel())
			.deviceOs(entity.getDeviceOs())
			.deviceOsVersion(entity.getDeviceOsVersion())
			.appVersion(entity.getAppVersion())
			.createdAt(entity.getCreatedAt())
			.updatedAt(entity.getUpdatedAt())
			.build();
	}
}
