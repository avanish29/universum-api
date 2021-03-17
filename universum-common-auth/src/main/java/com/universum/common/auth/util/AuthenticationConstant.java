package com.universum.common.auth.util;

public final class AuthenticationConstant {
	private AuthenticationConstant() {}
	
	public static final String[] AUTH_WHITELIST = {"/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/authenticate/**", "/settings/**", "/messages/**", "/favicon.ico"};
	
	public static final String ACTUATOR_PATH = "/actuator/**";
	
	public static final String AUTHENTICATION_SCHEME_BEARER = "Bearer ";
}
