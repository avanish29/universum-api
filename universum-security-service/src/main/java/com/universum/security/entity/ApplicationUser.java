package com.universum.security.entity;

import lombok.Data;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "app_user", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) })
@Audited
public class ApplicationUser extends AbstractBaseEntity {
	private static final long serialVersionUID = -6436265863032188052L;
	private String username;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String emailAddress;

    private String emailTokenHash;
    private LocalDateTime emailTokenGeneratedTime;
    private int emailVerificationAttempts;
    private Boolean emailVerified;
    private LocalDateTime emailVerifiedTime;

    private Integer failedLoginAttempts = 0;
    private LocalDateTime lastLoginFailureTime;
    private LocalDateTime lastSuccessfulLoginTime;

    private LocalDateTime passwordResetTokenGeneratedTime;
    private LocalDateTime lastPasswordChangedTime;
    private String passwordResetToken;

    private Boolean active = true;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "app_user_roles",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<ApplicationRole> roles;
}
