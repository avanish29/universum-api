package com.universum.service.apigateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.universum.service.apigateway.repository.LoadBalancedUserDetailsRepository;

@Service
public class LoadBalancedUserDetailsService implements UserDetailsService {

	@Autowired
	private LoadBalancedUserDetailsRepository userDetailsRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userDetailsRepository.getByUserName(username);
	}

}
