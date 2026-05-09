package com.yapp.ndgl.lock;

@FunctionalInterface
public interface LockTask<T> {
    T execute() throws Throwable;
}
