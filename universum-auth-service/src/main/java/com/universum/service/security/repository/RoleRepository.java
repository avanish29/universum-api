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

package com.universum.service.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.universum.service.security.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	public int countByNameIgnoreCase(final String name);
	
	@Query(nativeQuery = true, value = "SELECT count(aur.user_id) from app_user_roles aur left join app_user au on aur.user_id = au.id where aur.role_id = :roleId and au.deleted = false")
	public int countUserByRoleId(@Param("roleId") final Long roleId);
	
	public Optional<Role> findOneByName(final String name);
	
}
