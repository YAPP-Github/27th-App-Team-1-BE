package com.yapp.ndgl.domain.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.yapp.ndgl.domain.common.lock.NamedLockRepository;
import com.yapp.ndgl.lock.DistributedLockRepository;

@Configuration
public class DistributedLockConfig {

	@Bean
	public DistributedLockRepository distributedLockRepository(final DataSource dataSource) {
		return new NamedLockRepository(dataSource);
	}
}
