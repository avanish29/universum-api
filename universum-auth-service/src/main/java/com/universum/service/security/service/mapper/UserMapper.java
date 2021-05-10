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

package com.universum.service.security.service.mapper;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.universum.service.security.domain.Role;
import com.universum.service.security.domain.User;
import com.universum.service.security.dto.request.CreateUserRequest;
import com.universum.service.security.dto.response.UserResponse;
import com.universum.service.security.repository.RoleRepository;

/**
 * Converter to convert user entity to DTO and vice versa.
 * 
 * @author Avanish
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

	/**
	 * Convert role DTO to entity.
	 * 
	 * @param createUserRequest - Instance of {@link CreateUserRequest} DTO.
	 * @param roleRepository - Instance of {@link RoleRepository} used to perform CRUD operation.
	 * @param passwordEncoder - Instance of {@link PasswordEncoder} used to encode password.
	 * @return - New instance of {@link User} entity.
	 */
    @Mapping(target = "deleted", ignore = true)
	@Mapping(target = "guid", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdOn", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "updatedOn", ignore = true)
	@Mapping(target = "active", ignore = true)
	@Mapping(target = "emailAddress", ignore = true)
	@Mapping(target = "emailTokenGeneratedTime", ignore = true)
	@Mapping(target = "emailTokenHash", ignore = true)
	@Mapping(target = "emailVerificationAttempts", ignore = true)
	@Mapping(target = "emailVerified", ignore = true)
	@Mapping(target = "emailVerifiedTime", ignore = true)
	@Mapping(target = "failedLoginAttempts", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "lastLoginFailureTime", ignore = true)
	@Mapping(target = "lastPasswordChangedTime", ignore = true)
	@Mapping(target = "lastSuccessfulLoginTime", ignore = true)
	@Mapping(target = "passwordHash", ignore = true)
	@Mapping(target = "passwordResetToken", ignore = true)
	@Mapping(target = "passwordResetTokenGeneratedTime", ignore = true)
	@Mapping(target="roles",ignore = true)
    User toEntity(final CreateUserRequest createUserRequest, @Context RoleRepository roleRepository, @Context PasswordEncoder passwordEncoder);

    /**
     * Convert role entity to DTO.
     * 
     * @param userEntity - Instance of {@link User} entity.
     * @return - New instance of {@link UserResponse} DTO.
     */
    @Mapping(target = "roles", expression = "java(mapRolesToString(userEntity.getRoles()))")
	UserResponse toResponse(final User userEntity);
    
    /**
     * Convert string value of roles to {@link Role} entities and assign it to {@link User} entity.
     * 
     * @param userEntity - Instance of {@link User} entity.
     * @param createUserRequest - Instance of {@link CreateUserRequest} DTO.
     * @param roleRepository - Instance of {@link RoleRepository} used to perform CRUD operation.
     */
    @AfterMapping
    default void mapRoles(@MappingTarget User userEntity, CreateUserRequest createUserRequest, @Context RoleRepository roleRepository) {
        createUserRequest.getRoles().parallelStream()
                .map(roleRepository::findOneByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(userEntity::addRole);
    }
    
    /**
     * Encode string password using encoder and set the encoded password to user entity.
     * 
     * @param userEntity - Instance of {@link User} entity.
     * @param createUserRequest - Instance of {@link CreateUserRequest} DTO.
     * @param passwordEncoder - Instance of {@link PasswordEncoder} used to encode password.
     */
    @AfterMapping
    default void mapPassword(@MappingTarget User userEntity, CreateUserRequest createUserRequest, @Context PasswordEncoder passwordEncoder) {
    	userEntity.setPasswordHash(passwordEncoder.encode(createUserRequest.getPassword()));
    }
	
    /**
     * Default implementation to map {@link Role} entity to string.
     * 
     * @param roles - Collection of {@link Role} entity.
     * @return - Collection of role names.
     */
	default Set<String> mapRolesToString(final Set<Role> roles) {
		if(CollectionUtils.isEmpty(roles)) return Collections.emptySet();
		return roles.stream().map(Role::getName).collect(Collectors.toSet());
	}
}
