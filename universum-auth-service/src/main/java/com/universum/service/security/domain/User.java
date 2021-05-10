
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

package com.universum.service.security.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.Hibernate;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.universum.common.domain.AuditingBaseModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * Represents a user entity.
 * 
 * @author Avanish
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) })
@Audited
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Where(clause = "deleted is NULL or deleted != true")
@SequenceGenerator(name = User.USER_SEQUENCE_GENERATOR_NAME, sequenceName = User.USER_SEQUENCE_GENERATOR_NAME, allocationSize = 1)
public class User extends AuditingBaseModel {
	private static final long serialVersionUID = -6436265863032188052L;
	
	public static final String USER_SEQUENCE_GENERATOR_NAME = "user_sequence";
	
	public static final int USERNAME_MAX_LENGTH = 50;
    public static final int USERNAME_MIN_LENGTH = 6;
    
    public static final int FIRST_NAME_MAX_LENGTH = 64;
    public static final int FIRST_NAME_MIN_LENGTH = 2;
    
    public static final int LAST_NAME_MAX_LENGTH = 64;
    public static final int LAST_NAME_MIN_LENGTH = 2;
	
	@Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = USER_SEQUENCE_GENERATOR_NAME)
    protected Long id;
	
	@Column(name = "username", unique = true, length = USERNAME_MAX_LENGTH, nullable = false)
	private String username;
	
	@Column(name = "password", nullable = false)
    private String passwordHash;
    
    @Column(name = "first_name", length = FIRST_NAME_MAX_LENGTH, nullable = false)
    private String firstName;
    
    @Column(name = "last_name", length = LAST_NAME_MAX_LENGTH, nullable = false)
    private String lastName;
    
    @Column(name = "email", nullable = false)
    private String emailAddress;

    @Column(name = "email_token")
    private String emailTokenHash;
    
    @Column(name = "email_token_time")
    private LocalDateTime emailTokenGeneratedTime;
    
    @Column(name = "email_verification_attempts")
    private int emailVerificationAttempts;
    
    @Column(name = "email_verified")
    private Boolean emailVerified;
    
    @Column(name = "email_verified_time")
    private LocalDateTime emailVerifiedTime;

    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts = 0;
    
    @Column(name = "last_login_failure_time")
    private LocalDateTime lastLoginFailureTime;
    
    @Column(name = "last_successful_login_time")
    private LocalDateTime lastSuccessfulLoginTime;

    @Column(name = "password_reset_token_generated_time")
    private LocalDateTime passwordResetTokenGeneratedTime;
    
    @Column(name = "last_password_changed_time")
    private LocalDateTime lastPasswordChangedTime;
    
    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "user_roles",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)},
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false, updatable = false)})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    @NotAudited
    private Set<Role> roles = new HashSet<>();
    
    public void addRole(@NonNull final Role role) {
    	if(this.roles == null) this.roles = new HashSet<>();
    	this.roles.add(role);
    }

    @Override
    public String toString() {
        return "ApplicationUser(" +
                "id = " + id + ", " +
                "username = " + username + ", " +
                "firstName = " + firstName + ", " +
                "lastName = " + lastName + ", " +
                "emailAddress = " + emailAddress + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        var user = (User) o;

        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
