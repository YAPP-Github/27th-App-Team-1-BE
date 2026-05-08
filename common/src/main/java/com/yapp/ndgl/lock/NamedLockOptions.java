package com.yapp.ndgl.domain.common.lock;

import com.yapp.ndgl.lock.LockOptions;

public record NamedLockOptions(String key, int timeoutSeconds) implements LockOptions {
}
