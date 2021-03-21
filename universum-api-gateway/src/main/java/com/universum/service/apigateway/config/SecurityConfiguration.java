package com.universum.service.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;

import com.universum.security.jwt.JWTTokenProvider;
import com.universum.security.util.AuthenticationConstant;
import com.universum.service.apigateway.security.JWTHeaderExchangeMatcher;
import com.universum.service.apigateway.security.JWTReactiveAuthenticationManager;
import com.universum.service.apigateway.security.JWTTokenAuthenticationConverter;
import com.universum.service.apigateway.security.UnauthorizedAuthenticationEntryPoint;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {
	private static final String[] AUTH_WHITELIST = {"/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/services/security/authenticate/**", "/authenticate/**", "/settings/**", "/services/label/messages/**", "/favicon.ico"};
	
	@Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, UnauthorizedAuthenticationEntryPoint entryPoint, JWTTokenProvider tokenProvider) {
		http.httpBasic().disable();
		http.formLogin().disable();
		http.csrf().disable();
		http.logout().disable();
		http.requestCache().requestCache(NoOpServerRequestCache.getInstance());
		http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
		http.exceptionHandling().authenticationEntryPoint(entryPoint)
			.and()
			.authorizeExchange().pathMatchers(AuthenticationConstant.ACTUATOR_PATH).permitAll()
			.and()
			.authorizeExchange().pathMatchers(HttpMethod.OPTIONS).permitAll()
			.and()
			.addFilterAt(webFilter(tokenProvider), SecurityWebFiltersOrder.AUTHENTICATION).authorizeExchange().pathMatchers(AUTH_WHITELIST).permitAll()
			.anyExchange().authenticated();
		return http.build();
	}
	
	@Bean
    public AuthenticationWebFilter webFilter(JWTTokenProvider tokenProvider) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(repositoryReactiveAuthenticationManager());
        authenticationWebFilter.setServerAuthenticationConverter(new JWTTokenAuthenticationConverter(tokenProvider));
        authenticationWebFilter.setRequiresAuthenticationMatcher(new JWTHeaderExchangeMatcher());
        authenticationWebFilter.setSecurityContextRepository(new WebSessionServerSecurityContextRepository());
        // NoOpServerSecurityContextRepository is used to for STATELESS sessions so no session or state is persisted between requests.
        // The client must send the Authorization header with every request.
        authenticationWebFilter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        return authenticationWebFilter;
    }
	
	@Bean
    public JWTReactiveAuthenticationManager repositoryReactiveAuthenticationManager() {
		return new JWTReactiveAuthenticationManager(reactiveUserDetailsService(), passwordEncoder());
    }
	
	@Bean
    public ReactiveUserDetailsService reactiveUserDetailsService() {
        return null;
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
