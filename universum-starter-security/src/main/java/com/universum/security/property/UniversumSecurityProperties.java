package com.universum.security.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "universum", ignoreUnknownFields = false)
public class UniversumSecurityProperties {
	private final Security security = new Security();
	
	public Security getSecurity() {
		return security;
	}
	
	public static class Security {
		private final Authentication authentication = new Authentication();
		private String[] userEndpoints = new String[] {};
		private String[] adminEndpoints = new String[] {};
		private String[] unsecuredEndpoints = new String[] {"/public**"};
		
		public Authentication getAuthentication() {
			return authentication;
		}
		
		public String[] getUserEndpoints() {
			return userEndpoints;
		}
		
		public String[] getAdminEndpoints() {
			return adminEndpoints;
		}
		
		public String[] getUnsecuredEndpoints() {
			return unsecuredEndpoints;
		}
		
		public static class Authentication {
			private final Jwt jwt = new Jwt();
			
			public Jwt getJwt() {
				return jwt;
			}
			
			public static class Jwt {
				private String secret = null;
				private long tokenValidityInSeconds = 1800; // 30 minutes
				private long tokenValidityInSecondsForRememberMe = 2592000; // 30 days
				
				public String getSecret() {
					return secret;
				}
				
				public void setSecret(String secret) {
					this.secret = secret;
				}
				
				public long getTokenValidityInSeconds() {
					return tokenValidityInSeconds;
				}
				
				public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
					this.tokenValidityInSeconds = tokenValidityInSeconds;
				}
				
				public long getTokenValidityInSecondsForRememberMe() {
					return tokenValidityInSecondsForRememberMe;
				}
				
				public void setTokenValidityInSecondsForRememberMe(long tokenValidityInSecondsForRememberMe) {
					this.tokenValidityInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
				}
			}			
		}
	}
}