package com.yapp.ndgl.domain.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

	@Primary
	@Bean
	@ConfigurationProperties("spring.datasource")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Primary
	@Bean
	@ConfigurationProperties("spring.datasource.hikari")
	public DataSource dataSource(
		@Qualifier("dataSourceProperties") DataSourceProperties dataSourceProperties
	) {
		return dataSourceProperties.initializeDataSourceBuilder()
			.type(HikariDataSource.class)
			.build();
	}
}
