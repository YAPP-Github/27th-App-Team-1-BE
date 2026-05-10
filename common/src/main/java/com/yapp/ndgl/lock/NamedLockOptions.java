package com.yapp.ndgl.lock;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record NamedLockOptions(String key, int timeoutSeconds) implements LockOptions {

	public static NamedLockOptions of(final String key, final int timeoutSeconds) {
		return NamedLockOptions.builder()
			.key(key)
			.timeoutSeconds(timeoutSeconds)
			.build();
	}
}
