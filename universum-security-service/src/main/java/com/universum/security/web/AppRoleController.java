package com.universum.security.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@RequestMapping(method = RequestMethod.GET)
    public UniversumPageResponse<RoleDTO> findAll(@Valid UniversumPageRequest pageRequest) {
        return roleService.findAllRoles(pageRequest);
    }
}
