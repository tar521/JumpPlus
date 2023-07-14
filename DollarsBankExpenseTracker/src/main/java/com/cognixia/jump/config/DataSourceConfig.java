package com.cognixia.jump.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class DataSourceConfig {
	
	@Autowired
	private Environment env;
	
	@Bean
	public DataSource customDataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(env.getProperty("spring.datasource.url"));
		config.setUsername(env.getProperty("spring.datasource.username"));
		config.setPassword(env.getProperty("spring.datasource.password"));
		config.setUsername(env.getProperty("spring.datasource.username"));
		config.setMaximumPoolSize(Integer.valueOf(env.getProperty("spring.datasource.hikari.maximum-pool-size")));
		config.setConnectionTimeout(Long.valueOf(env.getProperty("spring.datasource.hikari.connectionTimeout")));
		config.setMaxLifetime(Long.valueOf(env.getProperty("spring.datasource.hikari.maxLifetime")));
		
		return new HikariDataSource(config);
	}
	

}
