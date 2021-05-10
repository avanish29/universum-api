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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.Hibernate;

import com.universum.common.domain.AuditingBaseModel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a permission entity.
 * 
 * @author Avanish
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "permissions", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@SequenceGenerator(sequenceName = Permission.PERMISSION_SEQUENCE_GENERATOR_NAME, name = Permission.PERMISSION_SEQUENCE_GENERATOR_NAME, allocationSize = 1)
public class Permission extends AuditingBaseModel {
	private static final long serialVersionUID = 1487227621155711682L;
		
	public static final String PERMISSION_SEQUENCE_GENERATOR_NAME = "permission_sequence";
	
	public static final int NAME_MIN_LENGTH = 6;
    public static final int NAME_MAX_LENGTH = 40;

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = PERMISSION_SEQUENCE_GENERATOR_NAME)
    protected Long id;
	
	@Column(nullable = false, length = NAME_MAX_LENGTH)
    private String name;
    
    @Override
    public String toString() {
        return "ApplicationPermission(" +
                "id = " + id + ", " +
                "name = " + name + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        var that = (Permission) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
