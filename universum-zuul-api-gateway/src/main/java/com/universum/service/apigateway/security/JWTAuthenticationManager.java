package com.universum.service.apigateway.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import com.universum.common.exception.UniversumAPIException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthenticationManager implements AuthenticationManager {
	private final PasswordEncoder passwordEncoder;
	private final UserDetailsService userDetailsService;
	
	public JWTAuthenticationManager(final UserDetailsService userDetailsService, final PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	public Authentication authenticate(final Authentication authentication) {
		Assert.notNull(authentication, "Authentication can not be null.");
		if(authentication.isAuthenticated()) return authentication;
		
		UsernamePasswordAuthenticationToken tokenAuthentication = (UsernamePasswordAuthenticationToken)authentication;
		UserDetails userDetails = this.findUser(tokenAuthentication);
		if(userDetails != null) {
			if(!passwordEncoder.matches((String)tokenAuthentication.getCredentials(), userDetails.getPassword())) {
				log.trace("Unable to match password for username '{}'", userDetails.getUsername());
				throw new UniversumAPIException(HttpStatus.PRECONDITION_FAILED, "Invalid password");
			}
			tokenAuthentication = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), userDetails.getAuthorities());
		}
		return tokenAuthentication;
	}
	
	private UserDetails findUser(final UsernamePasswordAuthenticationToken authenticationToken) {
		UserDetails userDetails = null;
		if(authenticationToken != null && StringUtils.isNotBlank(authenticationToken.getName())) {
			try {
				log.info("Finding user by user name '{}'", authenticationToken.getName());
				userDetails = this.userDetailsService.loadUserByUsername(authenticationToken.getName());
			} catch(UsernameNotFoundException notFoundEx) {
				log.trace("Unable to find user by username '{}'", authenticationToken.getName());
				throw new UniversumAPIException(HttpStatus.PRECONDITION_FAILED, "Invalid username", notFoundEx);
			}
		}
		return userDetails;
	}
}
