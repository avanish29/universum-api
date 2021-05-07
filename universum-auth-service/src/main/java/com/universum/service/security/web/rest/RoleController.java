package com.universum.service.security.web.rest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.universum.service.security.web.rest.util.PathConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.universum.common.model.UniversumPageRequest;
import com.universum.common.model.UniversumPageResponse;
import com.universum.service.security.dto.request.CreateRoleRequest;
import com.universum.service.security.dto.response.RoleResponse;
import com.universum.service.security.service.RoleService;

@RestController
public class RoleController {
	@Autowired
	private RoleService roleService;
	
	@GetMapping(PathConstants.ROLES)
    public UniversumPageResponse<RoleResponse> findAll(@Valid UniversumPageRequest pageRequest) {
        return roleService.findAllRoles(pageRequest);
    }
	
	@PostMapping(PathConstants.ROLES)
	public ResponseEntity<RoleResponse> createRole(@RequestBody @Valid CreateRoleRequest roleRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(roleService.createRole(roleRequest));
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
	
	@PutMapping(PathConstants.ROLES_BY_ID)
	public ResponseEntity<RoleResponse> updateRole(@PathVariable String id, @RequestBody @Valid CreateRoleRequest roleRequest) {
		return ResponseEntity.ok().body(null);
	}
	
	@DeleteMapping(PathConstants.ROLES_BY_ID)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteRole(@PathVariable Long id) {
		roleService.deleteRole(id);
	}
}