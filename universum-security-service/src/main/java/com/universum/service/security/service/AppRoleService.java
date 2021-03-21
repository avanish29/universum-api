package com.universum.service.security.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.universum.common.model.UniversumPageRequest;
import com.universum.common.model.UniversumPageResponse;
import com.universum.service.security.dto.CreateRoleRequest;
import com.universum.service.security.dto.RoleResponse;
import com.universum.service.security.entity.ApplicationRole;
import com.universum.service.security.repository.AppRoleRepository;

@Service
public class AppRoleService {
	@Autowired
	private AppRoleRepository roleRepository;
	
	public UniversumPageResponse<RoleResponse> findAllRoles(final UniversumPageRequest appPageRequest) {
		Optional<Direction> sortDir = Direction.fromOptionalString(appPageRequest.getOrder());
		PageRequest pageRequest = PageRequest.of(appPageRequest.getOffset(), appPageRequest.getLimit(), sortDir.isPresent() ? sortDir.get() : Direction.ASC , appPageRequest.getSort());
        
		Page<ApplicationRole> pageResponse = roleRepository.findAllByDeletedFalse(pageRequest);
		List<RoleResponse> roleDTOList = Optional.of(pageResponse.getContent()).orElseGet(Collections::emptyList).stream()
                .map(RoleResponse::fromEntity).collect(Collectors.toList());
        
        return new UniversumPageResponse<>(pageResponse.getTotalElements(), pageResponse.getTotalPages(), pageResponse.getNumber(), roleDTOList);
    }
	
	public RoleResponse findById(final Long id) {
		return RoleResponse.fromEntity(roleRepository.findByIdNotDeleted(id));
	}
	
	public RoleResponse createRole(final CreateRoleRequest roleRequest) {
		if(roleRepository.countByNameAndDeletedFalseIgnoreCase(roleRequest.getName()) > 0) {
			throw new ValidationException(String.format("Role with name '%s' already exists!", roleRequest.getName()));
		}
		ApplicationRole appRole = new ApplicationRole();
		appRole.setName(roleRequest.getName());
		appRole.setDescription(roleRequest.getDescription());
		appRole = roleRepository.save(appRole);
		return RoleResponse.fromEntity(appRole);
	}
	
	public void deleteRole(final Long id) {
		ApplicationRole appRole = roleRepository.findByIdNotDeleted(id);
		if(BooleanUtils.isTrue(appRole.getIsSystem())) {
			throw new ValidationException("Special roles can not be deleted!");
		}
		if(roleRepository.countUserByRoleId(appRole.getId()) > 0) {
			throw new ValidationException("Role is being used and cannot be deleted!");
		}
		appRole.setDeleted(Boolean.TRUE);
		roleRepository.save(appRole);
	}
	
	public void deleteRoles(final List<Long> ids) {
		if(CollectionUtils.isNotEmpty(ids)) {
			ids.stream().forEach(id -> this.deleteRole(id));
		}
	}
	
}