package com.yapp.ndgl.application.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 비동기 작업 처리를 위한 설정.
 * 기본 executor는 가상 스레드를 사용하여 스레드 풀 고갈 없이 비동기 작업을 처리한다.
 */
@Configuration
@EnableAsync
public class AsyncConfig {

	/**
	 * 기본 @Async executor.
	 * - 가상 스레드 사용 → 스레드 풀 크기 제한 없음
	 * - executor 미지정 @Async 메서드에 자동 적용
	 */
	@Bean
	public Executor taskExecutor() {
		return new VirtualThreadTaskExecutor("virtual-");
	}
}
