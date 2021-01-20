package com.universum.service.apigateway.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class JWTReactiveAuthenticationManager implements ReactiveAuthenticationManager {
	private final PasswordEncoder passwordEncoder;
	private final ReactiveUserDetailsService userDetailsService;
	
	public JWTReactiveAuthenticationManager(final ReactiveUserDetailsService userDetailsService, final PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	public Mono<Authentication> authenticate(final Authentication authentication) {
		Assert.notNull(authentication, "Authentication can't be null.");
		if(authentication.isAuthenticated()) {
			return Mono.just(authentication);
		}
		return Mono.just(authentication)
				.switchIfEmpty(Mono.defer(() -> raiseException(HttpStatus.UNAUTHORIZED, "Invalid Credentials.")))
				.cast(UsernamePasswordAuthenticationToken.class)
				.flatMap(this::findUser)
				.switchIfEmpty(Mono.defer(() -> {
					log.debug("No user found with username '{}'.", authentication.getName());
					return raiseException(HttpStatus.UNAUTHORIZED, "Invalid Username.");
				}))
				.publishOn(Schedulers.parallel())
				.onErrorResume(exception -> {
					log.error("Error occurred while finding user with username '{}'.",authentication.getName(), exception);
					return raiseException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while getting user details.");
				})
				.filter(userDetails -> passwordEncoder.matches((String) authentication.getCredentials(), userDetails.getPassword()))
				.switchIfEmpty(Mono.defer(() -> {
					log.debug("Password didn't match for user with username '{}'.", authentication.getName());
					return raiseException(HttpStatus.UNAUTHORIZED, "Invalid Password.");
				}))
				.map(userDetails -> buildAuthToken(authentication, userDetails));
	}
	
	private <T> Mono<T> raiseException(final HttpStatus status, final String msg) {
        return Mono.error(new ResponseStatusException(status, msg));
    }
	
	private UsernamePasswordAuthenticationToken buildAuthToken(final Authentication authentication, final UserDetails userDetails) {
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), userDetails.getAuthorities());
		authToken.setDetails(userDetails);
		return authToken;
	}
	
	private Mono<UserDetails> findUser(final UsernamePasswordAuthenticationToken authenticationToken) {
		if(authenticationToken != null && StringUtils.isNotBlank(authenticationToken.getName()) && SecurityContextHolder.getContext().getAuthentication() == null) {
			log.info("Finding user by username '{}'", authenticationToken.getName());
			return this.userDetailsService.findByUsername(authenticationToken.getName());
		}
		return null;
	}

}
