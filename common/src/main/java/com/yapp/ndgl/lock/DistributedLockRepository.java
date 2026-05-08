package com.yapp.ndgl.lock;

public interface DistributedLockRepository {

    void withLock(LockOptions options, Runnable task);

    LockOptions createOptions(String key, int timeoutSeconds);
}
