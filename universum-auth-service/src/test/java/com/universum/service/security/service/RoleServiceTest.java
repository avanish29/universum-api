package com.universum.service.security.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ValidationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.universum.common.dto.request.PageSearchRequest;
import com.universum.common.dto.response.PageSearchResponse;
import com.universum.common.enums.RoleType;
import com.universum.common.exception.NotFoundException;
import com.universum.service.security.domain.Permission;
import com.universum.service.security.domain.Role;
import com.universum.service.security.dto.request.CreateRoleRequest;
import com.universum.service.security.dto.request.UpdateRoleRequest;
import com.universum.service.security.dto.response.RoleResponse;
import com.universum.service.security.repository.RoleRepository;

@ExtendWith(SpringExtension.class)
class RoleServiceTest {
	@Mock
    private RoleRepository roleRepository;
    
    @InjectMocks
    private RoleService roleService;
    
    // Test for roleService.findAllRoles() START
    
    @Test
    void shouldReturnAllRoles() throws Exception {
    	List<Role> roles = mockRoles();
    	Mockito.when(roleRepository.findAll(Mockito.any(Pageable.class))).thenReturn(new PageImpl<>(roles));
    	
    	PageSearchResponse<RoleResponse> pageResult = roleService.findAllRoles(new PageSearchRequest());
    	Assertions.assertNotNull(pageResult);
    	Assertions.assertEquals(5, pageResult.getTotalItems());
    	Assertions.assertEquals(0, pageResult.getCurrentPage());
    	Assertions.assertEquals(1, pageResult.getTotalPages());
    	
    	Assertions.assertNotNull(pageResult.getContents());
    	Assertions.assertEquals(5, pageResult.getContents().size());
    }
    
    @Test
    void shouldThrowErrorWhenRequestIsNullForAllRoles() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
        	roleService.findAllRoles(null);
        }, "pageSearchRequest is marked non-null but is null");
    }
    
    // Test for roleService.findAllRoles() END
    
    // Test for roleService.findById() START
    
    @Test
    void shouldReturnResultfindById() throws Exception {
    	Long roleToUpdate = 1L;
    	List<Role> roles = mockRoles();
    	Mockito.when(roleRepository.findById(roleToUpdate)).thenReturn(roles.stream().filter(role -> roleToUpdate.equals(role.getId())).findFirst());
    	
    	RoleResponse result = roleService.findById(roleToUpdate);
    	Assertions.assertNotNull(result);
    	Assertions.assertEquals(roleToUpdate, result.getId());
    	Assertions.assertEquals("SUPER_ADMIN", result.getName());
    	Assertions.assertEquals("Super Admin users has access to all tasks.", result.getDescription());
    	Assertions.assertEquals(Boolean.TRUE, result.getIsSystem());
    }
    
    @Test
    void shouldThrowExceptionWhenRoleByIdDoesNotExist() throws Exception {
    	Long roleToUpdate = 1L;
    	Mockito.when(roleRepository.findById(roleToUpdate)).thenReturn(Optional.empty());
    	
    	Assertions.assertThrows(NotFoundException.class, () -> {
    		roleService.findById(roleToUpdate);
         }, "Entity Role with id 1 not found");
    }
    
    @Test
    void shouldThrowExceptionWhenUserIDIsNullForFindById() throws Exception {
    	 Assertions.assertThrows(IllegalArgumentException.class, () -> {
    		 roleService.findById(null);
         }, "id is marked non-null but is null");
    }
    
    // Test for roleService.findById() END
    
    // Test for roleService.createRole() START
    
    @Test
    void shouldCreateNewRole() throws Exception {
    	Role role = mockRole(6L, RoleType.ROLE_USER, "FINANCE", "Finance", Boolean.FALSE, null);
    	
    	Mockito.when(roleRepository.countByNameIgnoreCase(role.getName())).thenReturn(0);
    	Mockito.when(roleRepository.save(Mockito.any(Role.class))).thenReturn(role);
    	
    	RoleResponse result = roleService.createRole(mockCreateRequest(role.getRoleType(), role.getName(), role.getDescription()));
    	Assertions.assertNotNull(result);
    	Assertions.assertEquals(role.getId(), result.getId());
    	Assertions.assertEquals(role.getName(), result.getName());
    	Assertions.assertEquals(role.getDescription(), result.getDescription());
    	Assertions.assertEquals(role.getIsSystem(), result.getIsSystem());
    }
    
    @Test
    void shouldThrowExceptionWhenRoleWithNameAlreadyPresent() throws Exception {
    	Role role = mockRole(6L, RoleType.ROLE_USER, "FINANCE", "Finance", Boolean.FALSE, null);
    	CreateRoleRequest request = mockCreateRequest(role.getRoleType(), role.getName(), role.getDescription());
    	
    	Mockito.when(roleRepository.countByNameIgnoreCase(role.getName())).thenReturn(1);
    	
    	Assertions.assertThrows(ValidationException.class, () -> {
    			roleService.createRole(request);
        	}, "Role with name 'FINANCE' already exists!");
    }
    
    @Test
    void shouldThrowExceptionWhenCreateRoleRequestIsNull() throws Exception {
    	Assertions.assertThrows(IllegalArgumentException.class, () -> {
   		 roleService.createRole(null);
        }, "roleRequest is marked non-null but is null");
    }
    
    // Test for roleService.createRole() END
    
    // Test for roleService.updateRole() START
    
    @Test
    void shouldUpdateRole() throws Exception {
    	Role role = mockRole(6L, RoleType.ROLE_USER, "FINANCE", "Finance", Boolean.FALSE, null);
    	Role updatedRole = mockRole(6L, RoleType.ROLE_USER, "FINANCE", "Updated Role Description.", Boolean.FALSE, null);
    	
    	Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
    	Mockito.when(roleRepository.save(Mockito.any(Role.class))).thenReturn(updatedRole);
    	
    	RoleResponse result = roleService.updateRole(role.getId(), mockUpdateRequest(updatedRole.getDescription()));
    	
    	Assertions.assertNotNull(result);
    	Assertions.assertEquals(role.getId(), result.getId());
    	Assertions.assertEquals(role.getName(), result.getName());
    	Assertions.assertEquals(role.getDescription(), result.getDescription());
    	Assertions.assertEquals(role.getIsSystem(), result.getIsSystem());
    }
    
    @Test
    void shouldThrowExceptionWhenTryToUpdateSystemRole() throws Exception {
    	Role role = mockRole(6L, RoleType.ROLE_ADMIN, "SUPER_ADMIN", "Super Admin users has access to all tasks.", Boolean.TRUE, null);
    	UpdateRoleRequest updateRoleRequest = mockUpdateRequest("Test");
    	
    	Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
    	
    	Assertions.assertThrows(ValidationException.class, () -> {roleService.updateRole(6L, updateRoleRequest);}, "Special roles can not be updated!");
    }
    
    @Test
    void shouldThrowExceptionWhenRoleIdIsNullForUpdateRole() throws Exception {
    	UpdateRoleRequest updateRoleRequest = mockUpdateRequest("Test");
    	
    	Assertions.assertThrows(IllegalArgumentException.class, () -> { roleService.updateRole(null, updateRoleRequest); }, "roleId is marked non-null but is null");
    }
    
    @Test
    void shouldThrowExceptionWhenUpdateRoleRequestIsNullForUpdateRole() throws Exception {
    	Assertions.assertThrows(IllegalArgumentException.class, () -> { roleService.updateRole(6L, null); }, "updateRoleRequest is marked non-null but is null");
    }
    
    // Test for roleService.updateRole() END
    
    // Test for roleService.deleteRole() START
    
    @Test
    void shouldDeleteRole() throws Exception {
    	Role role = mockRole(6L, RoleType.ROLE_USER, "FINANCE", "Finance", Boolean.FALSE, null);
    	Role deletedRole = mockRole(6L, RoleType.ROLE_USER, "_6_FINANCE", "Finance", Boolean.FALSE, null);
    	deletedRole.setDeleted(Boolean.TRUE);
    	
    	Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
    	Mockito.when(roleRepository.countUserByRoleId(role.getId())).thenReturn(0);
    	Mockito.when(roleRepository.save(Mockito.any(Role.class))).thenReturn(deletedRole);
    	
    	
    	RoleResponse result = roleService.deleteRole(role.getId());
    	
    	Assertions.assertNotNull(result);
    	Assertions.assertEquals(deletedRole.getId(), result.getId());
    	Assertions.assertEquals(deletedRole.getName(), result.getName());
    	Assertions.assertEquals(deletedRole.getDescription(), result.getDescription());
    	Assertions.assertEquals(deletedRole.getIsSystem(), result.getIsSystem());
    }
    
    @Test
    void shouldThrowExceptionWhenRoleIdNullForDeleteRole() throws Exception {
    	Assertions.assertThrows(IllegalArgumentException.class, () -> { roleService.deleteRole(null); }, "id is marked non-null but is null");
    }
    
    @Test
    void shouldThrowExceptionWhenTryToDeleteSystemRole() throws Exception {
    	Role role = mockRole(6L, RoleType.ROLE_ADMIN, "SUPER_ADMIN", "Super Admin users has access to all tasks.", Boolean.TRUE, null);
    	
    	Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
    	
    	Assertions.assertThrows(ValidationException.class, () -> { roleService.deleteRole(6L); }, "Special roles can not be deleted!");
    }
    
    @Test
    void shouldThrowExceptionWhenRoleHasAssociatedUsers() throws Exception {
    	Role role = mockRole(6L, RoleType.ROLE_USER, "FINANCE", "Finance", Boolean.FALSE, null);
    	
    	Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
    	Mockito.when(roleRepository.countUserByRoleId(role.getId())).thenReturn(2);
    	
    	Assertions.assertThrows(ValidationException.class, () -> { roleService.deleteRole(6L); }, "Role is being used and cannot be deleted!");
    }
    
    // Test for roleService.deleteRole() END
    
    // Test for roleService.deleteRoles() START
    
    @Test
    void shouldDeleteAllRoles() throws Exception {
    	Role role = mockRole(6L, RoleType.ROLE_USER, "FINANCE", "Finance", Boolean.FALSE, null);
    	Role deletedRole = mockRole(6L, RoleType.ROLE_USER, "_6_FINANCE", "Finance", Boolean.FALSE, null);
    	deletedRole.setDeleted(Boolean.TRUE);
    	
    	Mockito.when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
    	Mockito.when(roleRepository.countUserByRoleId(role.getId())).thenReturn(0);
    	Mockito.when(roleRepository.save(Mockito.any(Role.class))).thenReturn(deletedRole);
    	
    	
    	List<RoleResponse> result = roleService.deleteRoles(List.of(role.getId()));
    	
    	Assertions.assertNotNull(result);
    	Assertions.assertEquals(1, result.size());
    }
    
    @Test
    void shouldReturnNullWhenDeleteWithEmptyCollection() throws Exception {
    	List<RoleResponse> result = roleService.deleteRoles(List.of());
    	
    	Assertions.assertNotNull(result);
    	Assertions.assertEquals(0, result.size());
    }
    
    // Test for roleService.deleteRoles() END
    
    private List<Role> mockRoles() {
    	return List.of(
    			mockRole(1L, RoleType.ROLE_ADMIN, "SUPER_ADMIN", "Super Admin users has access to all tasks.", Boolean.TRUE, null),
    			mockRole(2L, RoleType.ROLE_ADMIN, "CONFIG_ADMIN", "Config Admin users has access to all configuration tasks.", Boolean.TRUE, null),
    			mockRole(3L, RoleType.ROLE_USER, "UNIT_MANAGER", "Head of Department", Boolean.FALSE, null),
    			mockRole(4L, RoleType.ROLE_USER, "SUPERVISOR", "Supervising staff and organizing and monitoring work processes", Boolean.FALSE, null),
    			mockRole(5L, RoleType.ROLE_USER, "VICE_PRESIDENT", "VicePresident", Boolean.FALSE, null)
    	);
    }
    
    private Role mockRole(final Long roleId, final RoleType roleType, final String roleName, final String roleDescription, final Boolean isSystem, final Set<Permission> permissions) {
    	Role role = new Role();
    	role.setId(roleId);
    	role.setRoleType(roleType);
    	role.setName(roleName);
    	role.setDescription(roleDescription);
    	role.setIsSystem(isSystem);
    	role.setDeleted(Boolean.FALSE);
    	return role;
    }
    
    private CreateRoleRequest mockCreateRequest(final RoleType roleType, final String roleName, final String roleDescription) {
    	CreateRoleRequest createRequest = new CreateRoleRequest();
    	createRequest.setRoleType(roleType.name());
    	createRequest.setName(roleName);
    	createRequest.setDescription(roleDescription);
    	return createRequest;
    }

    private UpdateRoleRequest mockUpdateRequest(final String roleDescription) {
    	UpdateRoleRequest updateRoleRequest = new UpdateRoleRequest();
    	updateRoleRequest.setDescription(roleDescription);
    	return updateRoleRequest;
    }
}
