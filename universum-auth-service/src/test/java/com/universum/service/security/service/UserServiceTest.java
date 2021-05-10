package com.universum.service.security.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.universum.common.dto.request.PageSearchRequest;
import com.universum.common.dto.response.PageSearchResponse;
import com.universum.common.enums.RoleType;
import com.universum.common.exception.NotFoundException;
import com.universum.service.security.domain.Role;
import com.universum.service.security.domain.User;
import com.universum.service.security.dto.response.UserResponse;
import com.universum.service.security.repository.UserRepository;
import com.universum.service.security.service.mapper.UserMapper;
import com.universum.service.security.service.mapper.UserMapperImpl;

@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    
    @Spy
    private UserMapper userMapper = new UserMapperImpl();

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnAllUsers() throws Exception {
        List<User> mockUsers = mockUsers();
        Mockito.when(userRepository.findAll(Mockito.any(Pageable.class))).thenReturn(new PageImpl<User>(mockUsers));
        
        PageSearchResponse<UserResponse> result = userService.getAllUsers(new PageSearchRequest());
        Assertions.assertNotNull(result);
    }
    
    @Test
    void shouldThrowErrorWhenRequestIsNullForAllUsers() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
        	userService.getAllUsers(null);
        }, "pageSearchRequest is marked non-null but is null");
    }
    
    @Test
    void shouldReturnUserById() throws Exception {
    	Long userIdToLookFor = 1L;
    	Mockito.when(userRepository.findOneWithRolesById(userIdToLookFor)).thenReturn(Optional.of(mockUsers().get(0)));
    	
    	UserResponse result = userService.findUserById(userIdToLookFor);
    	Assertions.assertNotNull(result);
    }
    
    @Test
    void shouldThrowExceptionWhenUserByIdIsNotExist() throws Exception {
    	Long userIdToLookFor = 1L;
    	Mockito.when(userRepository.findOneWithRolesById(userIdToLookFor)).thenReturn(Optional.empty());
    	
    	Assertions.assertThrows(NotFoundException.class, () -> {
         	userService.findUserById(userIdToLookFor);
         }, "Entity User with id 1 not found");
    }
    
    @Test
    void shouldThrowExceptionWhenUserIDIsNullInUserById() throws Exception {
    	 Assertions.assertThrows(IllegalArgumentException.class, () -> {
         	userService.findUserById(null);
         }, "id is marked non-null but is null");
    }

    private List<User> mockUsers() {
        User user = new User();
        user.setId(1L);
        user.setActive(true);
        user.setEmailAddress("test@test.com");
        user.setUsername("test");
        user.setFirstName("Junit");
        user.setLastName("Test");
        
        Role role = new Role();
        role.setRoleType(RoleType.ROLE_ADMIN);
        role.setName("SUPER_ADMIN");
        role.setDescription("Super Admin users has access to all tasks.");
        
        user.addRole(role);
        return List.of(user);
    }
}
