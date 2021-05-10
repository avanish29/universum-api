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

import com.universum.service.security.domain.Role;
import com.universum.service.security.dto.request.CreateRoleRequest;
import com.universum.service.security.dto.response.RoleResponse;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapping;

/**
 * Converter to convert role entity to DTO and vice versa.
 * 
 * @author Avanish
 */
@Mapper
public interface RoleMapper {
    public static final RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    /**
     * Convert role DTO to entity.
     * 
     * @param createRoleRequest - Instance of {@link CreateRoleRequest} DTO.
     * @return - New instance of {@link Role} entity.
     */
    @Mapping(target = "deleted", ignore = true)
	@Mapping(target = "guid", ignore = true)
	@Mapping(target = "version", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "createdOn", ignore = true)
	@Mapping(target = "updatedBy", ignore = true)
	@Mapping(target = "updatedOn", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "isSystem", ignore = true)
	@Mapping(target = "permissions", ignore = true)
	Role toEntity(CreateRoleRequest createRoleRequest);
    
    /**
     * Convert role entity to DTO.
     * 
     * @param roleEntity - Instance of {@link Role} entity.
     * @return - New instance of {@link RoleResponse} DTO.
     */
    RoleResponse toDto(Role roleEntity);
}
