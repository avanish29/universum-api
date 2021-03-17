package com.universum.service.apigateway.security;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;

import com.universum.common.auth.jwt.JWTTokenProvider;
import com.universum.common.auth.util.AuthenticationConstant;

import reactor.core.publisher.Mono;

public class JWTTokenAuthenticationConverter implements ServerAuthenticationConverter {
	private static final Predicate<String> MATCH_BEARER_LENGTH = authValue -> authValue.length() > AuthenticationConstant.AUTHENTICATION_SCHEME_BEARER.length();
	private static final UnaryOperator<String> ISOLATE_BEARER_VALUE = authValue -> authValue.substring(AuthenticationConstant.AUTHENTICATION_SCHEME_BEARER.length(), authValue.length());
	
	private final JWTTokenProvider tokenProvider;

	public JWTTokenAuthenticationConverter(JWTTokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public Mono<Authentication> convert(ServerWebExchange exchange) {
		return Mono.justOrEmpty(exchange)
				.map(this::getTokenFromRequest)
				.filter(Objects::nonNull)
				.filter(MATCH_BEARER_LENGTH)
				.map(ISOLATE_BEARER_VALUE)
				.filter(token -> !StringUtils.isEmpty(token))
				.map(tokenProvider::getAuthentication)
				.filter(Objects::nonNull);
	}
	
	public String getTokenFromRequest(final ServerWebExchange serverWebExchange) {
        String token = serverWebExchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return StringUtils.isBlank(token) ? StringUtils.EMPTY : token;
    }

}
