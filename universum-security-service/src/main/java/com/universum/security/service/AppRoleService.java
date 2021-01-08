package com.universum.security.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.universum.security.dto.RoleResponse;
import com.universum.security.repository.AppRoleRepository;

@Service
public class AppRoleService {
	@Autowired
	private AppRoleRepository roleRepository;
	
	public List<RoleResponse> findAllRoles() {
        return Optional.of(roleRepository.findAllByDeletedFalse()).orElseGet(Collections::emptyList).stream()
                .map(role -> RoleResponse.fromEntity(role))
                .collect(Collectors.toList());
    }
}
