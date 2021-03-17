package com.universum.service.apigateway.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universum.common.auth.dto.AuthenticationRequest;
import com.universum.common.auth.dto.AuthenticationResponse;
import com.universum.common.auth.dto.UserDetailsResponse;
import com.universum.common.auth.jwt.JWTTokenProvider;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {
	@Autowired
	private JWTTokenProvider tokenProvider;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping
	public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest authRequest) {
		Authentication authenticationToken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
		Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
		UserDetailsResponse user = (UserDetailsResponse) authentication.getPrincipal();
		return ResponseEntity.ok()
				.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION)
                .header(HttpHeaders.AUTHORIZATION, tokenProvider.createToken(authentication))
                .body(AuthenticationResponse.fromEntity(user));
	}
}
