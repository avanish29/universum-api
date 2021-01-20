package com.universum.service.apigateway.service;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

import com.universum.service.apigateway.dto.LoggedInUser;
import com.universum.service.apigateway.service.remote.RemoteUserDetailsService;

import reactor.core.publisher.Mono;

public class LoadBalancedReactiveUserDetailsService implements ReactiveUserDetailsService {
	@Autowired
	private ObjectProvider<RemoteUserDetailsService> remoteUserDetailsService;
	
	@Override
	public Mono<UserDetails> findByUsername(final String username) {
		LoggedInUser result = remoteUserDetailsService.getIfAvailable().getByUserName(username);
		return result == null ? Mono.empty() : Mono.just(result);
	}

}
