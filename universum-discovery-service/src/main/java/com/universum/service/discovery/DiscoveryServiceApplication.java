package com.universum.service.discovery;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import com.universum.common.UniversumApplication;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@SpringBootApplication
@EnableEurekaServer
@EnableConfigServer
@EnableAdminServer
public class DiscoveryServiceApplication extends UniversumApplication {
	public static void main(String[] args) {
		runApp(args, DiscoveryServiceApplication.class);
	}
}
