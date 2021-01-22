package com.universum.service.apigateway.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
	@NotEmpty
	private String username;
	@NotEmpty
	private String password;
}
