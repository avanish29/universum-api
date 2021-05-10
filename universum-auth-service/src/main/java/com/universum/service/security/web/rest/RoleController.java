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

package com.universum.service.security.web.rest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.universum.common.dto.request.PageSearchRequest;
import com.universum.common.dto.response.PageSearchResponse;
import com.universum.service.security.dto.request.CreateRoleRequest;
import com.universum.service.security.dto.request.UpdateRoleRequest;
import com.universum.service.security.dto.response.RoleResponse;
import com.universum.service.security.service.RoleService;
import com.universum.service.security.web.rest.util.PathConstants;

/**
 * Rest resource to perform operation on role resource.
 * 
 * @author Avanish
 *
 */
@RestController
public class RoleController {
	@Autowired
	private RoleService roleService;
	
	@GetMapping(PathConstants.ROLES)
    public PageSearchResponse<RoleResponse> findAll(@Valid PageSearchRequest pageRequest) {
        return roleService.findAllRoles(pageRequest);
    }
	
	@PostMapping(PathConstants.ROLES)
	public ResponseEntity<RoleResponse> createRole(@RequestBody @Valid CreateRoleRequest roleRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(roleRequest));
	}
	
	@PutMapping(PathConstants.ROLES_BY_ID)
	public ResponseEntity<RoleResponse> updateRole(@PathVariable Long id, @RequestBody @Valid UpdateRoleRequest updateRoleRequest) {
		RoleResponse updatedEntity = roleService.updateRole(id, updateRoleRequest);
		return ResponseEntity.ok(updatedEntity);
	}
	
	@DeleteMapping(PathConstants.ROLES)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteRoles(@RequestParam("ids") String selectedRoles) {
		List<Long> selectedIds = Arrays.asList(selectedRoles.split(",")).stream().map(id -> Long.parseLong(id.trim())).collect(Collectors.toList());
		roleService.deleteRoles(selectedIds);
	}
	
	@GetMapping(PathConstants.ROLES_BY_ID)
    public ResponseEntity<RoleResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok().body(roleService.findById(id));
    }
	
	@DeleteMapping(PathConstants.ROLES_BY_ID)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteRole(@PathVariable Long id) {
		roleService.deleteRole(id);
	}
}
