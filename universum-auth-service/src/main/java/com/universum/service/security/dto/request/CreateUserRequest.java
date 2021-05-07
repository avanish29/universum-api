package com.universum.service.security.dto.request;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUserRequest implements Serializable {
	private static final long serialVersionUID = -3779637161477037500L;

	@NotBlank(message = "Username is required")
	@Size(max = 255)
    private String username;
	
	@NotBlank(message = "First name is required")
	@Size(max = 255)
    private String firstName;
	
	@NotBlank(message = "Last name is required")
	@Size(max = 255)
    private String lastName;
	
	@NotBlank(message = "Email is required") 
	@Email
	@Size(max = 255)
    private String email;
	
	@NotBlank(message = "Password is required")
    private String password;
    
	@NotBlank(message = "Repassword is required")
    private String rePassword;
    
	@NotNull(message = "Roles are required")
    @Size(min = 1)
    private Set<@NotBlank String> roles;
}
