package com.universum.service.security;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.universum.common.UniversumApplication;

@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "securityAuditAware")
public class UniversumSecurityApplication extends UniversumApplication {
	public static void main(String[] args) {
		runApp(args, UniversumSecurityApplication.class);
	}
}
