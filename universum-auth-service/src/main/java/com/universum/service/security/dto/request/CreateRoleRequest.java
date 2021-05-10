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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.universum.common.enums.RoleType;
import com.universum.common.validator.constraint.ValidEnum;
import lombok.Data;

@Data
public class CreateRoleRequest {
	@ValidEnum(enumClass = RoleType.class, message = "Role type must be either 'ROLE_ADMIN' or 'ROLE_USER'.")
	private String roleType;

	@NotBlank()
	@Size(max = 255)
	@Pattern(regexp = "^[a-zA-Z0-9]([a-zA-Z0-9_]*[a-zA-Z0-9])?$")
    private String name;
	
	@Size(max = 255)
	private String description;
}
