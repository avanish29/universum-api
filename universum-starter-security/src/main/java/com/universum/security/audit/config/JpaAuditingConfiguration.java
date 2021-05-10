package com.universum.security.audit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.universum.security.audit.SecurityAuditAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = SecurityAuditAware.SECURITY_AUDIT_AWARE)
public class JpaAuditingConfiguration {
	@Bean
	public SecurityAuditAware securityAuditAware() {
		return new SecurityAuditAware();
	}
}
