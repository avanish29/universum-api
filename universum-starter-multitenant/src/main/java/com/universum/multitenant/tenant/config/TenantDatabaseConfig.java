package com.universum.multitenant.tenant.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.universum.multitenant.catalog.config.CatalogDatabaseConfig;

import lombok.extern.slf4j.Slf4j;

/**
 * Define strategy for multi tenant support in hibernate.
 * 
 * @author Avanish
 *
 */
@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties({TenantConfigProperties.class})
@ComponentScan(basePackages = { TenantDatabaseConfig.UNIVERSUM_SERVICE_PACKAGE_STRUCTURE })
@EnableJpaRepositories(basePackages = { TenantDatabaseConfig.UNIVERSUM_SERVICE_PACKAGE_STRUCTURE },
        entityManagerFactoryRef = TenantDatabaseConfig.TENANT_ENTITY_MANAGER_FACTORY_BEAN_NAME,
        transactionManagerRef = TenantDatabaseConfig.TENANT_TRANSACTION_MANAGER_BEAN_NAME)
@Slf4j
public class TenantDatabaseConfig {
	public static final String UNIVERSUM_SERVICE_PACKAGE_STRUCTURE = "com.universum.service";
	public static final String TENANT_ENTITY_MANAGER_FACTORY_BEAN_NAME = "tenantEntityManagerFactory";
	public static final String TENANT_TRANSACTION_MANAGER_BEAN_NAME = "tenantTransactionManager";
	
	@Bean(name = "tenantJpaVendorAdapter")
	JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}
	
	@Bean(name = "tenantTransactionManager")
    public JpaTransactionManager transactionManager(@Qualifier("tenantEntityManagerFactory") EntityManagerFactory tenantEntityManager) {
        return new JpaTransactionManager(tenantEntityManager);
    }
	
	@Bean(name = "datasourceBasedMultitenantConnectionProvider")
    @ConditionalOnBean(name = CatalogDatabaseConfig.CATALOG_ENTITY_MANAGER_FACTORY_BEAN_NAME)
    public MultiTenantConnectionProvider multiTenantConnectionProvider() {
        // Autowires the multi connection provider
        return new DataSourceBasedMultiTenantConnectionProviderImpl();
    }
	
	@Bean(name = "currentTenantIdentifierResolver")
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver(final TenantConfigProperties tenantConfigProperties) {
        return new CurrentTenantIdentifierResolverImpl(tenantConfigProperties.getDefaultTenant());
    }
	
	@Bean(name = "tenantEntityManagerFactory")
    @ConditionalOnBean(name = "datasourceBasedMultitenantConnectionProvider")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("datasourceBasedMultitenantConnectionProvider") MultiTenantConnectionProvider connectionProvider,
            @Qualifier("currentTenantIdentifierResolver") CurrentTenantIdentifierResolver tenantResolver) {
        var emfBean = new LocalContainerEntityManagerFactoryBean();
        //All tenant related entities, repositories and service classes must be scanned
        emfBean.setPackagesToScan(UNIVERSUM_SERVICE_PACKAGE_STRUCTURE);
        emfBean.setJpaVendorAdapter(jpaVendorAdapter());
        emfBean.setPersistenceUnitName("tenantdb-persistence-unit");
        Map<String, Object> properties = new HashMap<>();
        properties.put(AvailableSettings.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        properties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        properties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
        properties.put(AvailableSettings.IMPLICIT_NAMING_STRATEGY, SpringImplicitNamingStrategy.class.getName());
        properties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, SpringPhysicalNamingStrategy.class.getName());
        properties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.PostgreSQL10Dialect");
        properties.put(AvailableSettings.SHOW_SQL, true);
        properties.put(AvailableSettings.FORMAT_SQL, true);
        properties.put(AvailableSettings.HBM2DDL_AUTO, "none");
        emfBean.setJpaPropertyMap(properties);
        log.info("Configured entity manager factory.");
        return emfBean;
    }
}
