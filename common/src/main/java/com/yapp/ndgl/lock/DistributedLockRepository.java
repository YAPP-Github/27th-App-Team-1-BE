package com.yapp.ndgl.repository;

public interface DistributedLockRepository {

    void withNamedLock(final String lockKey, final int timeoutSeconds, final Runnable task);
}
