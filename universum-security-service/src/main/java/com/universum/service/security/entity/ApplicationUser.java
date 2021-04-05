package com.universum.service.security.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import com.universum.common.jpa.domin.AbstractBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "app_user", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) })
@Audited
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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

    private Boolean active = Boolean.TRUE;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "app_user_roles",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<ApplicationRole> roles = new HashSet<>();
}
