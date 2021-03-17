package com.universum.service.apigateway.service;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.universum.common.auth.repository.UserDetailsRepository;

import reactor.core.publisher.Mono;

@Service
public class LoadBalancedReactiveUserDetailsService implements ReactiveUserDetailsService {
	@Autowired
	private ObjectProvider<UserDetailsRepository> userDetailsRepository;
	
	@Override
	public Mono<UserDetails> findByUsername(final String username) {
		UserDetails result = userDetailsRepository.getObject().getByUserName(username);
		return result == null ? Mono.empty() : Mono.just(result);
	}

}
