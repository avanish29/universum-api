package com.universum.service.i18service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class I18ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(I18ServiceApplication.class, args);
	}

}
