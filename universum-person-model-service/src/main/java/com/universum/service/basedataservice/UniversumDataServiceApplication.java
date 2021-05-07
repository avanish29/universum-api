package com.universum.service.basedataservice;

import com.universum.common.UniversumApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "securityAuditAware")
public class UniversumDataServiceApplication extends UniversumApplication {
    public static void main(String[] args) {
        runApp(args, UniversumDataServiceApplication.class);
    }
}
