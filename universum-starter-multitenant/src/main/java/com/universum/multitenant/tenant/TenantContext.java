package com.universum.multitenant.tenant;

/**
 * Class used to store the tenant identifier for each request.
 * 
 * @author Avanish
 */
public final class TenantContext {
	private static final ThreadLocal<String> CURRENT_TENANT = new InheritableThreadLocal<>();
	
	private TenantContext() {}
	
	/**
	 * Return the current tenant identifier for the request.
	 * @return - String value of tenant ID.
	 */
	public static String getCurrentTenant() {
		return CURRENT_TENANT.get();
	}
	
	/**
	 * Set the current tenant identifier for the request.
	 * @param tenantId - Current request tenant ID.
	 */
	public static void setCurrentTenant(final String tenantId) {
		CURRENT_TENANT.set(tenantId);
	}
	
	/**
	 * Clear the current request value for this thread-local variable.
	 */
	public static void clear() {
		CURRENT_TENANT.remove();
	}
}
