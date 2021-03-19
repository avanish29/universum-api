package com.universum.service.apigateway.filter.pre;

import java.util.Set;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

public class JWTTokenRelayFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return Boolean.TRUE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		Set<String> headers = (Set<String>) ctx.get("ignoredHeaders");
        // JWT tokens should be relayed to the resource servers
        headers.remove("authorization");
        return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 10000;
	}

}
