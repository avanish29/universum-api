/**
 * Copyright (c) 2021-present Universum Systems. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

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
