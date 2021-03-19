package com.universum.service.security.dto;

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
