package com.universum.service.label;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UniversumLabelApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniversumLabelApplication.class, args);
	}

}
