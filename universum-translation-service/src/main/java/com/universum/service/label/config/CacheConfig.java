package com.universum.service.label.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.spring.cache.HazelcastCacheManager;

@Configuration
public class CacheConfig {
	@Bean
	public CacheManager cacheManager() {
		var clientConfig = new ClientConfig();
		clientConfig.getNetworkConfig().addAddress("localhost:5701");
		return new HazelcastCacheManager(HazelcastClient.newHazelcastClient(clientConfig));
	}
}
