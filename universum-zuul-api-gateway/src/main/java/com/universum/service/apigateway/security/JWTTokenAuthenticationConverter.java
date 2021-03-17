package com.universum.service.apigateway.security;

import java.util.function.UnaryOperator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import com.universum.common.auth.jwt.JWTTokenProvider;
import com.universum.common.auth.util.AuthenticationConstant;
import com.universum.common.exception.UniversumAPIException;

public class JWTTokenAuthenticationConverter implements AuthenticationConverter {
	private static final UnaryOperator<String> ISOLATE_AUTHENTICATION_SCHEME_BEARER_VALUE = authValue -> authValue.substring(AuthenticationConstant.AUTHENTICATION_SCHEME_BEARER.length(), authValue.length());
	
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
		if (!StringUtils.startsWithIgnoreCase(header, AuthenticationConstant.AUTHENTICATION_SCHEME_BEARER)) {
			return null;
		}
		
		if (header.equalsIgnoreCase(AuthenticationConstant.AUTHENTICATION_SCHEME_BEARER)) {
			throw new UniversumAPIException(HttpStatus.UNAUTHORIZED, "Incorrect username or password.");
		}
		
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)tokenProvider.getAuthentication(ISOLATE_AUTHENTICATION_SCHEME_BEARER_VALUE.apply(header));
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return authentication;
	}

}
