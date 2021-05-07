package com.universum.service.security.web.rest;

import com.universum.common.util.Constants;
import com.universum.security.jwt.JWTTokenProvider;
import com.universum.service.security.dto.request.AuthenticationRequest;
import com.universum.service.security.dto.response.AuthenticationResponse;
import com.universum.service.security.dto.response.UserResponse;
import com.universum.service.security.web.rest.util.PathConstants;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Profile("!" + Constants.PROFILE_OAUTH2)
public class AuthenticationController {
	@Autowired
	private JWTTokenProvider tokenProvider;
	
	@Autowired
	private AuthenticationManagerBuilder authenticationManagerBuilder;

	@PostMapping(PathConstants.AUTHENTICATE)
	public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest authRequest) {
		Authentication authenticationToken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
		var authentication = this.authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		UserResponse user = (UserResponse) authentication.getPrincipal();
		String jwtToken = tokenProvider.createToken(authentication, BooleanUtils.isTrue(authRequest.getRememberMe()));
		return ResponseEntity.ok()
				.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION)
                .header(HttpHeaders.AUTHORIZATION, jwtToken)
                .body(AuthenticationResponse.fromEntity(user));
	}
}
