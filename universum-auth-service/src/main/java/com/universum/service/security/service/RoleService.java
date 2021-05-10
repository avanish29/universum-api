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

package com.universum.service.security.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.validation.ValidationException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universum.common.dto.request.PageSearchRequest;
import com.universum.common.dto.response.PageSearchResponse;
import com.universum.common.exception.NotFoundException;
import com.universum.security.util.AuthenticationConstant;
import com.universum.service.security.domain.Role;
import com.universum.service.security.dto.request.CreateRoleRequest;
import com.universum.service.security.dto.request.UpdateRoleRequest;
import com.universum.service.security.dto.response.RoleResponse;
import com.universum.service.security.repository.RoleRepository;
import com.universum.service.security.service.mapper.RoleMapper;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing roles.
 * @author Avanish
 */
@Slf4j
@Service
@Transactional
@PreAuthorize("hasAnyRole('" + AuthenticationConstant.SYSTEM_ADMIN + "', '" + AuthenticationConstant.SUPER_ADMIN + "')")
public class RoleService {
	private static final Function<CreateRoleRequest, Role> ROLE_DTO_TO_ENTITY_MAPPER = RoleMapper.INSTANCE::toEntity;
	private static final Function<Role, RoleResponse> ROLE_ENTITY_TO_DTO_MAPPER = RoleMapper.INSTANCE::toDto;

	@Autowired
	private RoleRepository roleRepository;

	/**
	 * Returns a {@link PageSearchResponse} of {@link RoleResponse} meeting the paging restriction provided in the {@code PageSearchRequest} object.
	 *
	 * @param pageSearchRequest - Instance of {@link PageSearchRequest} contains paging restrictions.
	 * @return a page of {@link RoleResponse}
	 */
	@Transactional(readOnly = true)
	public PageSearchResponse<RoleResponse> findAllRoles(final PageSearchRequest pageSearchRequest) {
		log.info("Find all roles request {}", pageSearchRequest);
		Page<Role> pageResult = roleRepository.findAll(pageSearchRequest.pageable());
		PageSearchResponse<RoleResponse> pageSearchResponse = PageSearchResponse.of(pageResult, ROLE_ENTITY_TO_DTO_MAPPER);
		log.info("Find all roles response {}", pageSearchResponse);
		return pageSearchResponse;
    }
	
	/** 
	 * Return an instance of {@link RoleResponse} by {@literal Role.id}
	 * 
	 * @param id - Id must not be {@literal null}.
	 * @return - The instance of {@link RoleResponse} with the given id or {@literal NotFoundException} if none found.
	 */
	@Transactional(readOnly = true)
	public RoleResponse findById(final Long id) {
		return RoleResponse.fromEntity(findByIdElseThrows(id));
	}
	
	/**
	 * Create role and return the newly created user.
	 * 
	 * @param roleRequest - instance of {@link CreateRoleRequest} containing information about the role to be created.
	 * @return - An instance of newly created user {@link RoleResponse}. Throw {@link ValidationException} if role with name is already present.
	 */
	public RoleResponse createRole(@NonNull final CreateRoleRequest roleRequest) {
		if(roleRepository.countByNameIgnoreCase(roleRequest.getName()) > 0) {
			throw new ValidationException(String.format("Role with name '%s' already exists!", roleRequest.getName()));
		}
		var appRole = ROLE_DTO_TO_ENTITY_MAPPER.apply(roleRequest);
		appRole = roleRepository.save(appRole);
		return RoleResponse.fromEntity(appRole);
	}
	
	/**
	 * Update role and return updated instance.
	 * 
	 * @param roleId - Id of the role.
	 * @param updateRoleRequest - Instance of {@link UpdateRoleRequest} contains data.
	 * @return An instance of updated role {@link RoleResponse}; will throw an exception {@link NotFoundException} if role with id does not exist.
	 */
	public RoleResponse updateRole(@NonNull Long roleId, @NonNull UpdateRoleRequest updateRoleRequest) {
		var appRole = Optional.of(findByIdElseThrows(roleId))
				.filter(role -> BooleanUtils.isNotTrue(role.getIsSystem()))
				.map(role -> {
					role.setDescription(updateRoleRequest.getDescription());
					role = roleRepository.save(role);
					log.debug("Changed Information for Role: {}", role);
					return role;
				})
				.orElseThrow(() -> new ValidationException("Special roles can not be updated!"));	
		return ROLE_ENTITY_TO_DTO_MAPPER.apply(appRole);
	}
	
	/**
	 * Soft delete role from database. One can not delete the role of type system.
	 * 
	 * @param id - Id of the role.
	 * @exception ValidationException if role has associated users.
	 */
	public void deleteRole(final Long id) {
		var appRole = findByIdElseThrows(id);
		if(BooleanUtils.isTrue(appRole.getIsSystem())) {
			throw new ValidationException("Special roles can not be deleted!");
		}
		if(roleRepository.countUserByRoleId(appRole.getId()) > 0) {
			throw new ValidationException("Role is being used and cannot be deleted!");
		}
		appRole.setName(String.format("_%s_%s", String.valueOf(appRole.getId()), appRole.getName()));
		appRole.setDeleted(Boolean.TRUE);
		roleRepository.save(appRole);
	}
	
	/**
	 * Delete roles from database.
	 * 
	 * @param ids - collection of role's Id.
	 */
	public void deleteRoles(final List<Long> ids) {
		if(CollectionUtils.isNotEmpty(ids)) {
			ids.stream().forEach(this::deleteRole);
		}
	}
	
	/**
	 * Locates the role based on the id if exist else throw {@link NotFoundException}.
	 * 
	 * @param id - Id of the role.
	 * @return - An instance of role {@link Role}; will throw an exception {@link NotFoundException} if role with id does not exist.
	 */
	Role findByIdElseThrows(final Long id) {
		return roleRepository.findById(id).orElseThrow(() -> new NotFoundException(Role.class, id));
	}
	
}
