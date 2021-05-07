package com.universum.service.security.web.rest;

import java.net.URI;

import javax.validation.Valid;

import com.universum.service.security.web.rest.util.PathConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.universum.common.model.UniversumPageRequest;
import com.universum.common.model.UniversumPageResponse;
import com.universum.service.security.dto.request.CreateUserRequest;
import com.universum.service.security.dto.request.UpdateUserRequest;
import com.universum.service.security.dto.response.UserResponse;
import com.universum.service.security.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping(PathConstants.USERS)
    public UniversumPageResponse<UserResponse> getAllUsers(@Valid UniversumPageRequest pageRequest) {
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
