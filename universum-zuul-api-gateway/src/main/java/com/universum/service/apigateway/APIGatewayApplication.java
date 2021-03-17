package com.universum.service.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.universum.service.apigateway.filter.pre.AuthorizationFilter;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.universum.common.auth.repository", "com.universum.service.apigateway"})
@EnableZuulProxy
@ComponentScan(basePackages = {"com.universum.common", "com.universum.service.apigateway"})
public class APIGatewayApplication {
	public static void main(String[] args) {
        SpringApplication.run(APIGatewayApplication.class, args);
    }
	
	@Bean
	public AuthorizationFilter authorizationFilter() {
        return new AuthorizationFilter();
    }
}
