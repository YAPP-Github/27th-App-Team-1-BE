package com.yapp.ndgl.clients.google.youtube.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * YouTube Data API RestClient 설정을 제공한다.
 */
@Configuration
public class YouTubeDataApiConfig {

	private static final String YOUTUBE_API_BASE_URL = "https://www.googleapis.com/youtube/v3";
	private static final int CONNECT_TIMEOUT_SECONDS = 5;
	private static final int READ_TIMEOUT_SECONDS = 10;

	/**
	 * YouTube Data API용 RestClient를 생성한다.
	 *
	 * @return base URL과 타임아웃이 설정된 RestClient
	 */
	@Bean
	public RestClient youTubeDataRestClient() {
		return RestClient.builder()
			.baseUrl(YOUTUBE_API_BASE_URL)
			.requestFactory(clientHttpRequestFactory())
			.build();
	}

	private ClientHttpRequestFactory clientHttpRequestFactory() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT_SECONDS));
		factory.setReadTimeout(Duration.ofSeconds(READ_TIMEOUT_SECONDS));
		return factory;
	}
}
