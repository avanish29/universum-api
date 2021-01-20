package com.universum.security.web;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universum.security.dto.UserDetails;

@RestController
@RequestMapping(path = "/users")
public class AppUserController {
	
	@GetMapping("/{username}")
    public UserDetails findByUserName(@PathVariable(name = "username") String userName) {
		Set<String> roles = new LinkedHashSet<>();
		roles.add("CONFIG_ADMIN");
		roles.add("LABLE_ADMIN");
		return UserDetails.builder().username("Admin").firstName("Avanish").lastName("Pandey").password("$2a$10$w77pv98eKtBRw/xQhTlYfuIVxoPPt0niHcEgLSJDZ6CR1L2V2k10e").accountNonExpired(true).accountNonLocked(true).credentialsNonExpired(true).emailAddress("admin@gmail.com").enabled(true).roles(roles).build();
    }
}
