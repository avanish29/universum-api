package com.universum.service.label;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.universum.common.UniversumApplication;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableJpaRepositories("com.universum.service.label.repository")
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "securityAuditAware")
public class UniversumLabelApplication extends UniversumApplication {
	public static void main(String[] args) {
		runApp(args, UniversumLabelApplication.class);
	}
}
