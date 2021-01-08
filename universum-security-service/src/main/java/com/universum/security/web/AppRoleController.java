package com.universum.security.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.universum.security.dto.RoleResponse;
import com.universum.security.service.AppRoleService;

@RestController
@RequestMapping(path = "/roles")
public class AppRoleController {
	@Autowired
	private AppRoleService roleService;
	
	@RequestMapping(method = RequestMethod.GET)
    public List<RoleResponse> findAll() {
        return roleService.findAllRoles();
    }
}
