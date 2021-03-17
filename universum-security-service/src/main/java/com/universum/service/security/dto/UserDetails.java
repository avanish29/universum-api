package com.universum.service.security.dto;

import java.io.Serializable;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetails implements Serializable {
	private static final long serialVersionUID = -3684638915669019377L;
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
