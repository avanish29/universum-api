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

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.universum.common.dto.request.PageSearchRequest;
import com.universum.common.dto.response.PageSearchResponse;
import com.universum.service.security.dto.request.CreateUserRequest;
import com.universum.service.security.dto.request.UpdateUserRequest;
import com.universum.service.security.dto.response.UserResponse;
import com.universum.service.security.service.UserService;
import com.universum.service.security.web.rest.util.PathConstants;

/**
 * Rest resource to perform operation on user resource.
 * 
 * @author Avanish
 *
 */
@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping(PathConstants.USERS)
    public PageSearchResponse<UserResponse> getAllUsers(@Valid PageSearchRequest pageRequest) {
        return userService.getAllUsers(pageRequest);
    }
	
	@PostMapping(PathConstants.USERS)
	public ResponseEntity<UserResponse> create(@RequestBody @Valid CreateUserRequest createRequest) {
		UserResponse createdEntity = userService.createUser(createRequest);
		return ResponseEntity.created(URI.create("/users/" + createdEntity.getId())).body(createdEntity);
	}
	
	@GetMapping(PathConstants.USERS_BY_ID)
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.findUserById(id));
    }
	
	@PutMapping(PathConstants.USERS_BY_ID)
	public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest updateRequest) {
		UserResponse updatedEntity = userService.updateUser(id, updateRequest);
		return ResponseEntity.ok(updatedEntity);
	}
	
	@DeleteMapping(PathConstants.USERS_BY_ID)
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
}
