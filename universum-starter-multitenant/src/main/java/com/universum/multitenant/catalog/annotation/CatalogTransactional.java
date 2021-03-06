package com.universum.multitenant.catalog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Transactional;

import com.universum.multitenant.catalog.config.CatalogDatabaseConfig;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(CatalogDatabaseConfig.CATALOG_TRANSACTION_MANAGER_BEAN_NAME)
public @interface CatalogTransactional {
	@AliasFor(attribute="readOnly", annotation=Transactional.class)
	boolean readOnly() default false;
}
