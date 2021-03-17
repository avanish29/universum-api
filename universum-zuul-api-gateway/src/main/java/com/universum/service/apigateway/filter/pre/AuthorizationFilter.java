package com.universum.service.apigateway.filter.pre;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

public class AuthorizationFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return Boolean.TRUE;
	}

	@Override
	public Object run() throws ZuulException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null && !(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetails = (UserDetails)auth.getPrincipal();
			RequestContext.getCurrentContext().addZuulRequestHeader("x-forwarded-user", userDetails.getUsername());
		}
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 10;
	}

}
