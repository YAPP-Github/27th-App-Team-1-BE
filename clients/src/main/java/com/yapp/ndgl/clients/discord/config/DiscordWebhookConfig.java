package com.yapp.ndgl.clients.discord.config;

import java.time.Duration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(DiscordWebhookProperties.class)
public class DiscordWebhookConfig {

	private static final int CONNECT_TIMEOUT_SECONDS = 3;
	private static final int READ_TIMEOUT_SECONDS = 5;

	@Bean
	public RestClient discordWebhookRestClient() {
		return RestClient.builder()
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
