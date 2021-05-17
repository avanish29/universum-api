package com.universum.multitenant.tenant.config;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.universum.multitenant.catalog.domain.TanentCatalog;
import com.universum.multitenant.catalog.service.TanentCatalogService;
import com.zaxxer.hikari.HikariDataSource;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
	private static final long serialVersionUID = 608505388186746774L;
	private transient Map<String, DataSource> dataSourcesByTenant = new TreeMap<>();
	
	@Autowired
	private transient TanentCatalogService tanantCatalogService;
	
	@Override
	protected DataSource selectAnyDataSource() {
		// This method is called more than once. So check if the data source map is empty. If it is then rescan master_tenant table for all tenant
        if (dataSourcesByTenant.isEmpty()) {
        	List<TanentCatalog> catalogTenants = tanantCatalogService.findAll();
            log.info("selectAnyDataSource() method call...Total tenants: {}", catalogTenants.size());
            for (TanentCatalog catalogTenant : catalogTenants) {
            	dataSourcesByTenant.put(catalogTenant.getTenantName(), createAndConfigureDataSource(catalogTenant));
            }
        }
        return this.dataSourcesByTenant.values().iterator().next();
	}

	@Override
	protected DataSource selectDataSource(final String tenantIdentifier) {
        return this.dataSourcesByTenant.computeIfAbsent(tenantIdentifier, dataSource -> {
        	TanentCatalog catalogTenant = tanantCatalogService.findByTenant(tenantIdentifier);
        	return createAndConfigureDataSource(catalogTenant);
        });
	}
	
	protected DataSource createAndConfigureDataSource(TanentCatalog catalogTenant) {
        var ds = new HikariDataSource();
        ds.setUsername(catalogTenant.getUserName());
        ds.setPassword(catalogTenant.getPassword());
        ds.setJdbcUrl(catalogTenant.getUrl());
        ds.setDriverClassName(catalogTenant.getDriverClass());
        // HikariCP settings - could come from the master_tenant table but hardcoded here for brevity
        // Maximum waiting time for a connection from the pool
        ds.setConnectionTimeout(20000);
        // Minimum number of idle connections in the pool
        ds.setMinimumIdle(3);
        // Maximum number of actual connection in the pool
        ds.setMaximumPoolSize(500);
        // Maximum time that a connection is allowed to sit idle in the pool
        ds.setIdleTimeout(300000);
        ds.setConnectionTimeout(20000);
        // Setting up a pool name for each tenant datasource
        String tenantConnectionPoolName = catalogTenant.getTenantName() + "-connection-pool";
        ds.setPoolName(tenantConnectionPoolName);
        log.info("Configured datasource : {}. Connection pool name : {}.", catalogTenant.getTenantName(), tenantConnectionPoolName);
        return ds;
    }
	
	@PostConstruct
	public void migrate() {
		log.info("Post construct database migration started for all tenants in catalog.");
		dataSourcesByTenant.putAll(tanantCatalogService.findAll().stream().collect(Collectors.toMap(TanentCatalog::getTenantName, this::createAndConfigureDataSource)));
		dataSourcesByTenant.entrySet().forEach(entry -> this.migrate(entry.getKey(), entry.getValue()));
		log.info("Post construct database migration finish for all tenants in catalog.");
	}
	
	private void migrate(@NonNull String tenantName, @NonNull DataSource tenantDataSource) {
		log.info("Start migartion for tenant : {}", tenantName);
		int successNumber = Flyway.configure().dataSource(tenantDataSource).baselineOnMigrate(true).load().migrate();
		log.info("Finish migartion for tenant : {} with applied migrations : {}", tenantName, successNumber);
	}
}
