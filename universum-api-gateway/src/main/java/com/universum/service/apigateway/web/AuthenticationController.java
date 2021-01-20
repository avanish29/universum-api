package com.universum.service.apigateway.web;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.universum.service.apigateway.dto.AuthenticationRequest;
import com.universum.service.apigateway.dto.AuthenticationResponse;
import com.universum.service.apigateway.dto.LoggedInUser;
import com.universum.service.apigateway.security.jwt.JWTTokenProvider;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/authenticate")
@Slf4j
public class AuthenticationController {
	@Autowired
	private Validator validator;
	
	@Autowired
	private JWTTokenProvider tokenProvider;
	
	@Autowired
	private ReactiveAuthenticationManager authenticationManager;

	@PostMapping
	public Mono<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest authRequest) {
		Set<ConstraintViolation<AuthenticationRequest>> voilations = this.validator.validate(authRequest);
		if(!voilations.isEmpty()) {
			voilations.stream().forEach(voilation -> log.debug("Constratint violation error {0}", voilation.getMessage())); 
			return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request"));
		}
		
		Authentication authenticationToken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
		Mono<Authentication> authentication = this.authenticationManager.authenticate(authenticationToken);
		authentication.doOnError(throwable -> {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad crendentials");
        });
		ReactiveSecurityContextHolder.withAuthentication(authenticationToken);
		return authentication.map(auth -> { 
			String jwt = tokenProvider.createToken(auth);
			LoggedInUser userDetails = (LoggedInUser)auth.getDetails();
            return new AuthenticationResponse(jwt, userDetails.getFirstName(), userDetails.getLastName());
        });
	}
}
