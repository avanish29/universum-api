package com.universum.multitenant.catalog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "universum.catalog.datasource", ignoreUnknownFields = false)
public class CatalogDatabaseConfigProperties {
	private String url;
    private String username;
    private String password;
    private String driverClassName;
    private long connectionTimeout;
    private int maxPoolSize;
    private long idleTimeout;
    private int minIdle;
    private String poolName;
    private boolean showSql = true;
    private boolean formatSql = true;
    private String hbm2ddl = "none";
}
