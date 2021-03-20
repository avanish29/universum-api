package com.universum.security.util;

public final class AuthenticationConstant {
	private AuthenticationConstant() {}
	
	public static final String PROFILE_OAUTH2 = "oauth2";
	
	public static final String[] AUTH_WHITELIST = {"/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/services/security/authenticate/**", "/authenticate/**", "/settings/**", "/services/label/messages/**", "/favicon.ico"};
	
	public static final String ACTUATOR_PATH = "/management/**";
	
	public static final String AUTHENTICATION_SCHEME_BEARER = "Bearer ";
}
