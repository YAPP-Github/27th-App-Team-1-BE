package com.yapp.ndgl.application.common.executor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompletableFutureExecutor {

	private static final Executor VIRTUAL_THREAD_EXECUTOR = Executors.newVirtualThreadPerTaskExecutor();

	/**
	 * 주어진 태스크들을 병렬 제출 후 Future 목록을 반환한다. (non-blocking)
	 * 기다릴지 여부는 호출부에서 결정한다.
	 * 개별 태스크 실패 시 null로 처리된다.
	 * Executor를 지정하지 않으면 기본 값으로 newVirtualThreadPerTaskExecutor 가 지정된다.
	 */
	@SafeVarargs
	public static <T> List<CompletableFuture<T>> invoke(final Supplier<T>... tasks) {
		return invoke(VIRTUAL_THREAD_EXECUTOR, tasks);
	}

	/**
	 * 주어진 태스크들을 병렬 제출 후 Future 목록을 반환한다. (non-blocking)
	 * 기다릴지 여부는 호출부에서 결정한다.
	 * 개별 태스크 실패 시 null로 처리된다.
	 */
	@SafeVarargs
	public static <T> List<CompletableFuture<T>> invoke(final Executor executor, final Supplier<T>... tasks) {
		return Arrays.stream(tasks)
			.map(task -> CompletableFuture
				.supplyAsync(task, executor)
				.<T>exceptionally(e -> {
					log.warn("태스크 실행 실패: {}", e.getMessage());
					return null;
				}))
			.toList();
	}

	/**
	 * 주어진 태스크들을 병렬 실행한다. 결과를 기다리지 않는다 (fire-and-forget).
	 * 개별 태스크 실패 시 로그만 남기고 계속 진행한다.
	 * Executor를 지정하지 않으면 기본 값으로 newVirtualThreadPerTaskExecutor 가 지정된다.
	 */
	public static void submit(final Runnable... tasks) {
		submit(VIRTUAL_THREAD_EXECUTOR, tasks);
	}


	/**
	 * 주어진 태스크들을 병렬 실행한다. 결과를 기다리지 않는다 (fire-and-forget).
	 * 개별 태스크 실패 시 로그만 남기고 계속 진행한다.
	 */
	public static void submit(final Executor executor, final Runnable... tasks) {
		Arrays.stream(tasks).forEach(task ->
			CompletableFuture.runAsync(task, executor)
				.exceptionally(e -> {
					log.warn("태스크 실행 실패: {}", e.getMessage());
					return null;
				})
		);
	}
}
