package com.universum.service.security.service;

import com.universum.common.dto.request.PageSearchRequest;
import com.universum.common.dto.response.PageSearchResponse;
import com.universum.service.security.domain.User;
import com.universum.service.security.dto.response.UserResponse;
import com.universum.service.security.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnAllUsers() throws Exception {
        List<User> mockUsers = mockUsers();
        Mockito.when(userRepository.findAll(Mockito.any(Pageable.class))).thenReturn(new PageImpl<User>(mockUsers));
        PageSearchResponse<UserResponse> result = userService.getAllUsers(new PageSearchRequest());
        Assertions.assertNotNull(result);
    }

    private List<User> mockUsers() {
        User user = new User();
        user.setId(1L);
        user.setActive(true);
        user.setEmailAddress("test@test.com");
        user.setUsername("test");
        user.setFirstName("Junit");
        user.setLastName("Test");
        return List.of(user);
    }
}
