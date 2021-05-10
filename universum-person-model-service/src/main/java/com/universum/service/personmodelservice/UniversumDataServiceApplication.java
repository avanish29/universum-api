package com.universum.service.personmodelservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.universum.common.UniversumApplication;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableTransactionManagement
public class UniversumDataServiceApplication extends UniversumApplication {
    public static void main(String[] args) {
        runApp(args, UniversumDataServiceApplication.class);
    }
}
