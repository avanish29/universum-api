package com.universum.service.security;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.universum.common.UniversumApplication;

@SpringBootApplication
@EnableDiscoveryClient
public class UniversumSecurityApplication extends UniversumApplication {
	public static void main(String[] args) {
		runApp(args, UniversumSecurityApplication.class);
	}
}
