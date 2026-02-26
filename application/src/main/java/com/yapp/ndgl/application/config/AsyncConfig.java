package com.yapp.ndgl.application.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 비동기 작업 처리를 위한 설정.
 * Photo 저장 등 시간이 오래 걸리는 부가 작업을 백그라운드에서 처리한다.
 */
@Configuration
@EnableAsync
public class AsyncConfig {

	/**
	 * Photo 저장용 비동기 Executor.
	 * - 코어 스레드 10개, 피크 시 최대 50개까지 확장
	 * - 큐 용량 500개 (총 550개 동시 처리 가능)
	 * - CallerRunsPolicy: 큐가 가득차면 호출 스레드에서 실행 (에러 방지)
	 */
	@Bean(name = "photoAsyncExecutor")
	public Executor photoAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("photo-async-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setAwaitTerminationSeconds(60);
		executor.initialize();
		return executor;
	}

	@Bean(name = "nearbyAsyncExecutor")
	public Executor nearbyAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("nearby-async-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setAwaitTerminationSeconds(60);
		executor.initialize();
		return executor;
	}
}
