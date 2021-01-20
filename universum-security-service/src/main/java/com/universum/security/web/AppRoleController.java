package com.universum.security.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universum.common.model.UniversumPageRequest;
import com.universum.common.model.UniversumPageResponse;
import com.universum.security.dto.RoleDTO;
import com.universum.security.service.AppRoleService;

@RestController
@RequestMapping(path = "/roles")
public class AppRoleController {
	@Autowired
	private AppRoleService roleService;
	
	@GetMapping()
    public UniversumPageResponse<RoleDTO> findAll(@Valid UniversumPageRequest pageRequest) {
        return roleService.findAllRoles(pageRequest);
    }
}
