package com.universum.multitenant.tenant.config;

import java.util.Optional;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import com.universum.multitenant.tenant.TenantContext;

/**
 * Class responsible for resolving the current tenant identifier.
 * 
 * @author Avanish
 *
 */
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {
	private final String defaultTenant;
	
	public CurrentTenantIdentifierResolverImpl(final String defaultTenant) {
		this.defaultTenant = defaultTenant;
	}
	
	@Override
	public String resolveCurrentTenantIdentifier() {
		return Optional.ofNullable(TenantContext.getCurrentTenant()).orElse(defaultTenant);
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return true;
	}

}
