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

package com.universum.service.security.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse implements Serializable {
	private static final long serialVersionUID = -1076903283800250209L;
	private String userName;
	private String firstName;
	private String lastName;
	private String picture;
	
	public static AuthenticationResponse fromEntity(UserResponse userEntity) {
		return AuthenticationResponse.builder().firstName(userEntity.getFirstName())
				.lastName(userEntity.getLastName())
				.userName(userEntity.getUsername())
				.build();
	}
}
