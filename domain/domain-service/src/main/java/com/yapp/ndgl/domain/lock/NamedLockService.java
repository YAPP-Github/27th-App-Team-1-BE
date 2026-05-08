package com.yapp.ndgl.domain.lock;

import org.springframework.stereotype.Service;

import com.yapp.ndgl.lock.DistributedLockRepository;
import com.yapp.ndgl.lock.LockOptions;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NamedLockService {

	private final DistributedLockRepository<LockOptions> distributedLockRepository;

	public void withLock(final LockOptions options, final Runnable task) {

	}
}
