package com.universum.service.configservice;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

import com.universum.common.UniversumApplication;

@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigServiceApplication extends UniversumApplication {
    public static void main(String[] args) {
        runApp(args, ConfigServiceApplication.class);
    }
}
