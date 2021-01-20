package com.universum.security.dto;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDetails implements Serializable {
	private static final long serialVersionUID = 8827362407675433977L;
	private String username;
	private String password;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private Set<String> roles;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;
}
