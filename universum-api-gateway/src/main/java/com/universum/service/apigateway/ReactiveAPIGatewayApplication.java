package com.universum.service.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.universum.common.auth.repository"})
@ComponentScan(basePackages = {"com.universum.common", "com.universum.service.apigateway"})
public class ReactiveAPIGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReactiveAPIGatewayApplication.class, args);
    }
}
