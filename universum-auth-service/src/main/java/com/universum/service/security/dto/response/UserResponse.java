package com.universum.service.security.dto.response;

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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.universum.common.model.BaseResponse;
import com.universum.service.security.domain.Role;
import com.universum.service.security.domain.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserResponse extends BaseResponse implements UserDetails {
	private static final long serialVersionUID = 8827362407675433977L;
	@ToString.Exclude
	private String username;
	@Getter(onMethod = @__( @JsonIgnore ))
	@ToString.Exclude
	private String passwordHash;
	@ToString.Exclude
    private String firstName;
	@ToString.Exclude
    private String lastName;
	@ToString.Exclude
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
    
    public static UserResponse fromEntity(final User entity, boolean appendRoles) {
    	if(entity == null) return null;
    	UserResponse response = builder()
                .id(entity.getId())
                .createdOn(entity.getCreatedOn())
                .createdBy(entity.getCreatedBy())
                .updatedOn(entity.getUpdatedOn())
                .updatedBy(entity.getUpdatedBy())
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
                .build();
    	if(appendRoles) {
    		response.setRoles(entity.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
    	}
    	return response;
    }

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>(roles.size());
		for (String role : roles) {
			Assert.isTrue(!role.startsWith("ROLE_"), () -> role + " cannot start with ROLE_ (it is automatically added)");
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
		}
		return authorities;
	}

	@Override
	@JsonIgnore
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
