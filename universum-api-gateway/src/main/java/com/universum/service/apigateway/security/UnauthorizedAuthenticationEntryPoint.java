package com.universum.service.apigateway.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class UnauthorizedAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
	private static final String WWW_AUTHENTICATE = "WWW-Authenticate";
	private static final String DEFAULT_REALM = "Realm";
	private static final String WWW_AUTHENTICATE_FORMAT = "Bearer realm=\"%s\"";
	
	private String headerValue = createHeaderValue(DEFAULT_REALM);
	
	@Override
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
		return Mono.fromRunnable(() -> {
			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			response.getHeaders().set(WWW_AUTHENTICATE, this.headerValue);
		});
	}
	
	public void setRealm(String realm) {
		this.headerValue = createHeaderValue(realm);
	}

	private static String createHeaderValue(String realm) {
		Assert.notNull(realm, "realm cannot be null");
		return String.format(WWW_AUTHENTICATE_FORMAT, realm);
	}

}
