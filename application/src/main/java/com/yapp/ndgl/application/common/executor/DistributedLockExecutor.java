package co.kr.woojjam.concurrency.repository.match;

import org.springframework.stereotype.Component;

import co.kr.woojjam.concurrency.common.lock.DistributedLockRepository;
import co.kr.woojjam.concurrency.common.lock.named.NamedLockOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NamedLockExecutor {

	private final DistributedLockRepository<NamedLockOptions> lockRepository;
	private final MatchRepository matchRepository;

	// public NamedLockExecutor(
	// 	@Qualifier("lockRepository") DistributedLockRepository<NamedLockOptions> lockRepository,
	// 	MatchRepository matchRepository
	// ) {
	// 	this.lockRepository = lockRepository;
	// 	this.matchRepository = matchRepository;
	// }

	public void executeWithLock(final NamedLockOptions namedLockOptions, final Runnable action) {
		lockRepository.withLock(namedLockOptions, action);
	}

	public void executeWithLockByNativeQuery(final NamedLockOptions namedLockOptions, final Runnable action) {
		matchRepository.getNamedLockByNativeQuery(namedLockOptions.key(), namedLockOptions.timeoutSeconds());
		action.run();
	}
}
