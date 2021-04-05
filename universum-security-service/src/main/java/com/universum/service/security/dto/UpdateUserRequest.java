package com.universum.service.security.dto;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserRequest {
	@NotBlank(message = "First name is required")
	@Size(max = 255, message = "")
    private String firstName;
	
	@NotBlank(message = "Last name is required")
	@Size(max = 255)
    private String lastName;
	
	@NotBlank(message = "Email is required") 
	@Email(message = "Please enter a valid email.")
	@Size(max = 255)
    private String email;
	
	@NotNull(message = "Roles are required")
    @Size(min = 1, max = 5, message = "Authorities must be between {min} and {max}")
    private Set<@NotBlank String> roles;
}
