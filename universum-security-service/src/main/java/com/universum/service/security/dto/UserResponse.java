package com.universum.service.security.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.universum.common.model.BaseResponse;
import com.universum.service.security.entity.ApplicationUser;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserResponse extends BaseResponse {
	private static final long serialVersionUID = 8827362407675433977L;
	private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private Set<String> roles;
    private LocalDateTime emailTokenGeneratedTime;
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
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .emailAddress(entity.getEmailAddress())
                .emailTokenGeneratedTime(entity.getEmailTokenGeneratedTime())
                .emailVerificationAttempts(entity.getEmailVerificationAttempts())
                .emailVerified(entity.getEmailVerified())
                .lastLoginFailureTime(entity.getLastLoginFailureTime())
                .lastSuccessfulLoginTime(entity.getLastSuccessfulLoginTime())
                .passwordResetTokenGeneratedTime(entity.getPasswordResetTokenGeneratedTime())
                .lastPasswordChangedTime(entity.getLastPasswordChangedTime())
                .build();
    }
}
