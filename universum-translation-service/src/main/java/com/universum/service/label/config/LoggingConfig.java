package com.universum.service.label.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.universum.service.label.logging.LoggingAspect;

@Configuration
@EnableAspectJAutoProxy
public class LoggingConfig {
	@Bean
	public LoggingAspect loggingAspect() {
		return new LoggingAspect();
	}
}
