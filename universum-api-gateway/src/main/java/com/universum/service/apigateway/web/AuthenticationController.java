package com.universum.service.apigateway.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.universum.common.auth.dto.AuthenticationRequest;
import com.universum.common.auth.dto.AuthenticationResponse;
import com.universum.common.auth.dto.UserDetailsResponse;
import com.universum.common.auth.jwt.JWTTokenProvider;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {
	@Autowired
	private JWTTokenProvider tokenProvider;
	
	@Autowired
	private ReactiveAuthenticationManager authenticationManager;

	@PostMapping
	public Mono<ResponseEntity<AuthenticationResponse>> authenticate(@Valid @RequestBody AuthenticationRequest authRequest) {
		Authentication authenticationToken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
		Mono<Authentication> authentication = this.authenticationManager.authenticate(authenticationToken);
		authentication.doOnError(throwable -> {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect username or password.");
        });
		ReactiveSecurityContextHolder.withAuthentication(authenticationToken);
		return authentication.map(auth -> {
			UserDetailsResponse user = (UserDetailsResponse) auth.getPrincipal();
			return ResponseEntity.ok()
					.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION)
					.header(HttpHeaders.AUTHORIZATION, tokenProvider.createToken(auth))
					.body(AuthenticationResponse.fromEntity(user));
        });
	}
}
