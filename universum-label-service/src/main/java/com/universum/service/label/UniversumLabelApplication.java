package com.universum.service.label;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.universum.service.label", "com.universum.common"})
public class UniversumLabelApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniversumLabelApplication.class, args);
	}

}
