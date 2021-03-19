package com.universum.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.universum.security.util.AuthenticationConstant;


public class JWTFilter extends GenericFilterBean {
	
	private final JWTTokenProvider jwtTokenProvider;
	
	public JWTFilter(JWTTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String bearerToken = resolveToken(httpServletRequest);
		if(StringUtils.isNotBlank(bearerToken) && this.jwtTokenProvider.validateToken(bearerToken)) {
			Authentication authentication = this.jwtTokenProvider.getAuthentication(bearerToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		chain.doFilter(request, response);
	}
	
	private String resolveToken(HttpServletRequest httpServletRequest) {
		String bearerToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
		if(StringUtils.isNotBlank(bearerToken) && bearerToken.trim().startsWith(AuthenticationConstant.AUTHENTICATION_SCHEME_BEARER)) {
			return bearerToken.substring(AuthenticationConstant.AUTHENTICATION_SCHEME_BEARER.length());
		}
		return null;
	}

}
