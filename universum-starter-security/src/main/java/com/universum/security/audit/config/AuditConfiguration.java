package com.universum.security.audit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.universum.security.audit.SecurityAuditAware;

@Configuration
public class AuditConfiguration {

	@Bean
	public SecurityAuditAware securityAuditAware() {
		return new SecurityAuditAware();
	}
}
