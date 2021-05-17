package com.universum.multitenant.catalog.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.universum.multitenant.catalog.domain.TanentCatalog;
import com.universum.multitenant.catalog.repository.TanentCatalogRepository;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties({CatalogDatabaseConfigProperties.class})
@ComponentScan(basePackages = { CatalogDatabaseConfig.CATALOG_PACKAGE })
@EnableJpaRepositories(basePackages = {CatalogDatabaseConfig.CATALOG_ENTITY_PACKAGE, CatalogDatabaseConfig.CATALOG_REPOSITORY_PACKAGE},
		entityManagerFactoryRef = CatalogDatabaseConfig.CATALOG_ENTITY_MANAGER_FACTORY_BEAN_NAME, 
		transactionManagerRef = CatalogDatabaseConfig.CATALOG_TRANSACTION_MANAGER_BEAN_NAME)
@Slf4j
public class CatalogDatabaseConfig {
	public static final String CATALOG_ENTITY_PACKAGE = "com.universum.multitenant.catalog.domain";
	public static final String CATALOG_REPOSITORY_PACKAGE = "com.universum.multitenant.catalog.repository";
	public static final String CATALOG_PACKAGE = "com.universum.multitenant.catalog";
	public static final String CATALOG_ENTITY_MANAGER_FACTORY_BEAN_NAME = "catalogEntityManagerFactory";
	public static final String CATALOG_TRANSACTION_MANAGER_BEAN_NAME = "catalogTransactionManager";
	
	@Autowired
	private CatalogDatabaseConfigProperties catalogDatabaseConfigProperties;
	
	/**
	 * Create catalog data source using catalog database config properties.
	 * 
	 * @return - A factory for connections to the physical catalog data source.
	 */
	@Bean
	public DataSource catalogDataSource() {
		var catalogDataSource = new HikariDataSource();
		catalogDataSource.setUsername(catalogDatabaseConfigProperties.getUsername());
		catalogDataSource.setPassword(catalogDatabaseConfigProperties.getPassword());
		catalogDataSource.setJdbcUrl(catalogDatabaseConfigProperties.getUrl());
		catalogDataSource.setDriverClassName(catalogDatabaseConfigProperties.getDriverClassName());
		catalogDataSource.setPoolName(catalogDatabaseConfigProperties.getPoolName());
		// HikariCP settings
		catalogDataSource.setMaximumPoolSize(catalogDatabaseConfigProperties.getMaxPoolSize());
		catalogDataSource.setMinimumIdle(catalogDatabaseConfigProperties.getMinIdle());
		catalogDataSource.setConnectionTimeout(catalogDatabaseConfigProperties.getConnectionTimeout());
		catalogDataSource.setIdleTimeout(catalogDatabaseConfigProperties.getIdleTimeout());
		log.info("Setup of catalogDataSource succeeded.");
		return catalogDataSource;
	}
	
	@Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean catalogEntityManagerFactory() {
		var emFactory = new LocalContainerEntityManagerFactoryBean();
		//Set the catalog data source
		emFactory.setDataSource(catalogDataSource());
		emFactory.setPackagesToScan(TanentCatalog.class.getPackage().getName(), TanentCatalogRepository.class.getPackage().getName());
		// Setting a name for the persistence unit as Spring sets it as 'default' if not defined
		emFactory.setPersistenceUnitName("UNIVERSUM-CATALOG-PERSISTENCE-UNIT");
		// Setting Hibernate as the JPA provider
		emFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		// Set the hibernate properties
		emFactory.setJpaProperties(hibernateProperties());
		log.info("Setup of catalogEntityManagerFactory succeeded.");
		return emFactory;
	}
	
	@Bean
	public JpaTransactionManager catalogTransactionManager(@Qualifier(CATALOG_ENTITY_MANAGER_FACTORY_BEAN_NAME) EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
	
	private Properties hibernateProperties() {
		var properties = new Properties();
		properties.put(AvailableSettings.IMPLICIT_NAMING_STRATEGY, SpringImplicitNamingStrategy.class.getName());
        properties.put(AvailableSettings.PHYSICAL_NAMING_STRATEGY, SpringPhysicalNamingStrategy.class.getName());
		properties.put(AvailableSettings.DIALECT, "org.hibernate.dialect.PostgreSQL10Dialect");
		properties.put(AvailableSettings.SHOW_SQL, catalogDatabaseConfigProperties.isShowSql());
		properties.put(AvailableSettings.FORMAT_SQL, catalogDatabaseConfigProperties.isFormatSql());
		properties.put(AvailableSettings.HBM2DDL_AUTO, catalogDatabaseConfigProperties.getHbm2ddl());
		return properties;
	}

}
