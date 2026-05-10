package com.yapp.ndgl.lock;

public interface DistributedLockRepository {

    <T> T withLock(final LockOptions options, final LockTask<T> task) throws Throwable;

    LockOptions createOptions(final String key, final int timeoutSeconds);
}
