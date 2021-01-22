package com.universum.service.apigateway.security;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class JWTHeaderRequestMatcher implements RequestMatcher {

	@Override
	public boolean matches(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames()).stream().filter(h -> h.equalsIgnoreCase(HttpHeaders.AUTHORIZATION)).findFirst().map(h -> {return true;}).orElse(false);
	}

}
