package com.yapp.ndgl.application.config;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class TestDatabaseConfig {

	private static final String MYSQL_CONTAINER_IMAGE = "mysql:8.0.35";
	private static final MySQLContainer<?> MYSQL_CONTAINER;

	static {
		MYSQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse(MYSQL_CONTAINER_IMAGE));
		MYSQL_CONTAINER.start();

		System.setProperty("spring.datasource.url", MYSQL_CONTAINER.getJdbcUrl());
		System.setProperty("spring.datasource.username", MYSQL_CONTAINER.getUsername());
		System.setProperty("spring.datasource.password", MYSQL_CONTAINER.getPassword());
		System.setProperty("spring.lock-datasource.url", MYSQL_CONTAINER.getJdbcUrl());
		System.setProperty("spring.lock-datasource.username", MYSQL_CONTAINER.getUsername());
		System.setProperty("spring.lock-datasource.password", MYSQL_CONTAINER.getPassword());
	}
}
