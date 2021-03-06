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
