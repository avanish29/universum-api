package com.universum.multitenant.tenant.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Transactional;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional("tenantTransactionManager")
public @interface TenantTransactional {
	@AliasFor(attribute="readOnly", annotation=Transactional.class)
	boolean readOnly() default false;
}
