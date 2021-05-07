package com.universum.service.security.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest implements Serializable {
	private static final long serialVersionUID = -7947443758525164855L;
	
	@NotEmpty(message = "Username is required")
	@Size(min = 1, max = 50, message = "The username '${validatedValue}' must be between {min} and {max} characters long")
	private String username;
	
	@ToString.Exclude
	@NotEmpty(message = "Password is required")
	private String password;
	
	private Boolean rememberMe;
}
