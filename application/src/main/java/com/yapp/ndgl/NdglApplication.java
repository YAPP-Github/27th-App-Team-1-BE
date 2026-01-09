package com.yapp.ndgl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * NDGL Application
 * - 멀티모듈 구조의 메인 애플리케이션
 * - scanBasePackages: 모든 모듈의 컴포넌트 스캔
 */
@SpringBootApplication(scanBasePackages = "com.yapp.ndgl")
public class NdglApplication {

	public static void main(String[] args) {
		SpringApplication.run(NdglApplication.class, args);
	}

}
