package com.yapp.ndgl.domain.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yapp.ndgl.domain.common.lock.NamedLockRepository;
import com.yapp.ndgl.lock.DistributedLockRepository;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DistributedLockConfig {

	@Bean
	@ConfigurationProperties("spring.lock-datasource")
	public DataSourceProperties lockDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@ConfigurationProperties("spring.lock-datasource.hikari")
	public DataSource lockDataSource(
		@Qualifier("lockDataSourceProperties") DataSourceProperties lockDataSourceProperties
	) {
		return lockDataSourceProperties.initializeDataSourceBuilder()
			.type(HikariDataSource.class)
			.build();
	}

	@Bean
	public DistributedLockRepository distributedLockRepository(
		@Qualifier("lockDataSource") final DataSource dataSource) {
		return new NamedLockRepository(dataSource);
	}
}
