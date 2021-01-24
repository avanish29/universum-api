package com.universum.service.apigateway.security;

import java.util.function.UnaryOperator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

public class JWTTokenAuthenticationConverter implements AuthenticationConverter {
	private static final String AUTHENTICATION_SCHEME_BEARER = "Bearer ";
	private static final UnaryOperator<String> ISOLATE_AUTHENTICATION_SCHEME_BEARER_VALUE = authValue -> authValue.substring(AUTHENTICATION_SCHEME_BEARER.length(), authValue.length());
	
	private final JWTTokenProvider tokenProvider;

	public JWTTokenAuthenticationConverter(JWTTokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}
	
	@Override
	public Authentication convert(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null) {
			return null;
		}

		header = header.trim();
		if (!StringUtils.startsWithIgnoreCase(header, AUTHENTICATION_SCHEME_BEARER)) {
			return null;
		}
		
		if (header.equalsIgnoreCase(AUTHENTICATION_SCHEME_BEARER)) {
			throw new BadCredentialsException("Empty bearer authentication token");
		}
		
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)tokenProvider.getAuthentication(ISOLATE_AUTHENTICATION_SCHEME_BEARER_VALUE.apply(header));
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return authentication;
	}

}
