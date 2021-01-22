package com.universum.service.apigateway.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universum.service.apigateway.dto.AuthenticationRequest;
import com.universum.service.apigateway.dto.AuthenticationResponse;
import com.universum.service.apigateway.security.JWTTokenProvider;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {
	@Autowired
	private JWTTokenProvider tokenProvider;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping
	public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest authRequest) {
		Authentication authenticationToken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
		Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		return new AuthenticationResponse(tokenProvider.createToken(authentication));
	}
}
