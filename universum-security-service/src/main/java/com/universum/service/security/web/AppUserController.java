package com.universum.service.security.web;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universum.common.model.UniversumPageRequest;
import com.universum.common.model.UniversumPageResponse;
import com.universum.service.security.dto.CreateUserRequest;
import com.universum.service.security.dto.UpdateUserRequest;
import com.universum.service.security.dto.UserDetails;
import com.universum.service.security.dto.UserResponse;
import com.universum.service.security.service.AppUserService;

@RestController
@RequestMapping(path = "/api/admin/security/users")
public class AppUserController {
	@Autowired
	private AppUserService userService;
	
	@GetMapping()
    public UniversumPageResponse<UserResponse> getAllUsers(@Valid UniversumPageRequest pageRequest) {
        return userService.getAllUsers(pageRequest);
    }
	
	@PostMapping
	public ResponseEntity<UserResponse> create(@RequestBody @Valid CreateUserRequest createRequest) {
		UserResponse createdEntity = userService.createUser(createRequest);
		return ResponseEntity.created(URI.create("/users/" + createdEntity.getId())).body(createdEntity);
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.findUserById(id));
    }
	
	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest updateRequest) {
		UserResponse updatedEntity = userService.updateUser(id, updateRequest);
		return ResponseEntity.ok(updatedEntity);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/{username}/details")
    public UserDetails getUserDetailsByUserName(@PathVariable(name = "username") String userName) {
		Set<String> roles = new LinkedHashSet<>();
		roles.add("CONFIG_ADMIN");
		roles.add("LABLE_ADMIN");
		return UserDetails.builder().username("Admin").firstName("Avanish").lastName("Pandey").password("$2a$10$7Snnw7jpkuc4W65UhNrjaeWzYlAfKTCynYmd9/vKv2AwBvhqg2U2y").accountNonExpired(true).accountNonLocked(true).credentialsNonExpired(true).emailAddress("admin@gmail.com").enabled(true).roles(roles).build();
    }
}
