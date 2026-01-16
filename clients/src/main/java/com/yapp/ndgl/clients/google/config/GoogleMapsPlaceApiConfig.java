package com.yapp.ndgl.clients.google.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import lombok.Getter;

/**
 * Google Maps RestClient와 API 키 설정을 제공한다.
 */
@Configuration
@Getter
public class GoogleMapsPlaceApiConfig {

	private static final String PLACES_API_BASE_URL = "https://places.googleapis.com/v1";
	private static final int CONNECT_TIMEOUT_SECONDS = 5;
	private static final int READ_TIMEOUT_SECONDS = 10;

	@Value("${google.maps.api-key}")
	private String apiKey;

	/**
	 * Google Maps Place API용 RestClient를 생성한다.
	 *
	 * @return base URL과 타임아웃이 설정된 RestClient
	 */
	@Bean
	public RestClient googleMapsPlaceRestClient() {
		return RestClient.builder()
			.baseUrl(PLACES_API_BASE_URL)
			.defaultHeader("X-Goog-Api-Key", apiKey)
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
