package com.universum.service.security.web;

import javax.validation.Valid;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universum.common.util.Constants;
import com.universum.security.jwt.JWTTokenProvider;
import com.universum.service.security.dto.AuthenticationRequest;
import com.universum.service.security.dto.AuthenticationResponse;
import com.universum.service.security.dto.UserResponse;

@RestController
@RequestMapping("/authenticate")
@Profile("!" + Constants.PROFILE_OAUTH2)
public class AuthenticationController {
	@Autowired
	private JWTTokenProvider tokenProvider;
	
	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;

	@PostMapping
	public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest authRequest) {
		Authentication authenticationToken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
		Authentication authentication = this.authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		UserResponse user = (UserResponse) authentication.getPrincipal();
		String jwtToken = tokenProvider.createToken(authentication, BooleanUtils.isTrue(authRequest.getRememberMe()));
		return ResponseEntity.ok()
				.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION)
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .body(AuthenticationResponse.fromEntity(user));
	}
}
