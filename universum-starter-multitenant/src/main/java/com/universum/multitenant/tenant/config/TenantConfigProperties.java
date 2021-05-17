package com.universum.multitenant.tenant.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties("universum.tenant")
public class TenantConfigProperties {
	private String defaultTenant;
}
