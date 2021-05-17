package com.universum.multitenant.tenant.web;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import com.universum.multitenant.tenant.TenantContext;

public class HeaderTenantInterceptor implements WebRequestInterceptor {
	private static final String TENANT_HEADER = "X-tenant";

	@Override
	public void preHandle(WebRequest request) throws Exception {
		String tenantName = request.getHeader(TENANT_HEADER);
		TenantContext.setCurrentTenant(tenantName);
		if(StringUtils.isNotBlank(tenantName)) {
			MDC.put("universum.tenant", tenantName);
		}
		
	}

	@Override
	public void postHandle(WebRequest request, ModelMap model) throws Exception {
		TenantContext.clear();
	}

	@Override
	public void afterCompletion(WebRequest request, Exception ex) throws Exception {
		// Do Nothing
	}

}
