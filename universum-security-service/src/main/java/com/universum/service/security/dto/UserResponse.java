package com.universum.service.security.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.universum.common.model.BaseResponse;
import com.universum.service.security.entity.ApplicationRole;
import com.universum.service.security.entity.ApplicationUser;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserResponse extends BaseResponse implements UserDetails {
	private static final long serialVersionUID = 8827362407675433977L;
	private String username;
	private String passwordHash;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private Set<String> roles;
    private LocalDateTime emailTokenGeneratedTime;
    private int failedLoginAttempts;
    private int emailVerificationAttempts;
    private Boolean emailVerified;
    private LocalDateTime emailVerifiedTime;
    private LocalDateTime lastLoginFailureTime;
    private LocalDateTime lastSuccessfulLoginTime;
    private LocalDateTime passwordResetTokenGeneratedTime;
    private LocalDateTime lastPasswordChangedTime;
    
    public static UserResponse fromEntity(final ApplicationUser entity) {
        return builder()
                .id(entity.getId())
                .created(entity.getCreated())
                .lastUpdate(entity.getLastUpdate())
                .username(entity.getUsername())
                .passwordHash(entity.getPasswordHash())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .emailAddress(entity.getEmailAddress())
                .emailTokenGeneratedTime(entity.getEmailTokenGeneratedTime())
                .failedLoginAttempts(entity.getFailedLoginAttempts())
                .emailVerificationAttempts(entity.getEmailVerificationAttempts())
                .emailVerified(entity.getEmailVerified())
                .lastLoginFailureTime(entity.getLastLoginFailureTime())
                .lastSuccessfulLoginTime(entity.getLastSuccessfulLoginTime())
                .passwordResetTokenGeneratedTime(entity.getPasswordResetTokenGeneratedTime())
                .lastPasswordChangedTime(entity.getLastPasswordChangedTime())
                .roles(entity.getRoles().stream().map(ApplicationRole::getName).collect(Collectors.toSet()))
                .build();
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
		for (String role : roles) {
			Assert.isTrue(!role.startsWith("ROLE_"), () -> role + " cannot start with ROLE_ (it is automatically added)");
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.passwordHash;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
