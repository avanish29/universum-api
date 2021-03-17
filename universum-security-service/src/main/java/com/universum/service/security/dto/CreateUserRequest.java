package com.universum.service.security.dto;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CreateUserRequest {
	@NotBlank
	@Size(max = 255)
    private String username;
	
	@NotBlank
	@Size(max = 255)
    private String firstName;
	
	@NotBlank
	@Size(max = 255)
    private String lastName;
	
	@NotBlank 
	@Email
	@Size(max = 255)
    private String email;
	
	@NotBlank
    private String password;
    
	@NotBlank
    private String rePassword;
    
	@NotNull
    @Size(min = 1)
    private Set<@NotBlank String> authorities;
}
