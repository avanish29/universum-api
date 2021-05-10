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

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.universum.service.security.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	public long countByUsername(final String userName);
	
	@EntityGraph(attributePaths = "roles")
	public Optional<User> findOneWithRolesByUsername(final String userName);
	
	public Optional<User> findOneByUsername(final String userName);
	
	@EntityGraph(attributePaths = "roles")
	public Optional<User> findOneWithRolesById(final Long id);
	
	@Modifying
	@Query("update User u set u.lastSuccessfulLoginTime = :lastSuccessfulLoginTime, u.failedLoginAttempts = :failedLoginAttempts where u.username = :userName")
	public int updateOnLoginSuccess(@Param("lastSuccessfulLoginTime") final LocalDateTime roleId, @Param("failedLoginAttempts") final int failedLoginAttempts, @Param("userName") final String userName);
	
	@Modifying
	@Query("update User u set u.lastLoginFailureTime = :lastLoginFailureTime, u.failedLoginAttempts = u.failedLoginAttempts + 1 where u.username = :userName")
	public int updateOnLoginFailure(@Param("lastLoginFailureTime") final LocalDateTime roleId, @Param("userName") final String userName);
}
