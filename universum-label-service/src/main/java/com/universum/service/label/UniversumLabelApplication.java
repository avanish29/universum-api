package com.universum.service.label;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.universum.common.UniversumApplication;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class UniversumLabelApplication extends UniversumApplication {
	public static void main(String[] args) {
		runApp(args, UniversumLabelApplication.class);
	}
}
