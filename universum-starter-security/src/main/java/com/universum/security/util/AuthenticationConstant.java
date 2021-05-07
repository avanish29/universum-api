package com.universum.security.util;

public final class AuthenticationConstant {
	private AuthenticationConstant() {}
	
	public static final String SYSTEM_ACCOUNT = "SYSTEM";
	
	public static final String SUPER_ADMIN = "SUPER_ADMIN";
	
	public static final String SYSTEM_ADMIN = "SYSTEM_ADMIN";
	
	public static final String CONFIG_ADMIN = "CONFIG_ADMIN";
	
	public static final String PROFILE_OAUTH2 = "oauth2";
	
	public static final String ACTUATOR_PATH = "/management/**";
	
	public static final String AUTHENTICATION_SCHEME_BEARER = "Bearer ";
}
