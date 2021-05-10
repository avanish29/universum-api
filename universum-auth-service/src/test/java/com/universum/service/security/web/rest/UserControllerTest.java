package com.universum.service.security.web.rest;

import com.universum.common.dto.request.PageSearchRequest;
import com.universum.common.dto.response.PageSearchResponse;
import com.universum.security.jwt.JWTTokenProvider;
import com.universum.service.security.dto.response.UserResponse;
import com.universum.service.security.service.UserService;
import com.universum.service.security.web.rest.util.PathConstants;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@WebMvcTest(UserController.class)
@TestPropertySource(locations = "classpath:application.properties")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

	private JWTTokenProvider jwtTokenProvider;

    @MockBean
    private UserService userService;

    @Test
	@WithMockUser(username = "test", roles = "SUPER_ADMIN")
    void shouldReturnAllUsers() throws Exception {
    	List<UserResponse> mockUsers = mockUsers();
		Mockito.when(userService.getAllUsers(new PageSearchRequest()))
				.thenReturn(PageSearchResponse.of(mockUsers.size(), 1, 0, mockUsers));

		this.mockMvc.perform(MockMvcRequestBuilders.get(PathConstants.USERS)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.totalItems", Matchers.is(6)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.totalPages", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.currentPage", Matchers.is(0)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.contents.length()", Matchers.is(6)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.contents[0].id", Matchers.is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.contents[0].username", Matchers.is("junit1")));
    }

	@Test
	@WithAnonymousUser()
	void shouldReturnUnauthorizedForUnitManagerRole() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get(PathConstants.USERS)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized())
				.andDo(MockMvcResultHandlers.print());
	}

    private List<UserResponse> mockUsers() {
    	return List.of(
    			UserResponse.builder().createdBy("SYSTEM").createdOn(LocalDateTime.now()).id(1L).emailAddress("test1@test.com").firstName("Junit1").lastName("Test1").username("junit1").updatedBy("SYSTEM").updatedOn(LocalDateTime.now()).roles(Set.of("SUPER_ADMIN")).build(),
    			UserResponse.builder().createdBy("SYSTEM").createdOn(LocalDateTime.now()).id(2L).emailAddress("test2@test.com").firstName("Junit2").lastName("Test2").username("junit2").updatedBy("SYSTEM").updatedOn(LocalDateTime.now()).roles(Set.of("SUPER_ADMIN")).build(),
    			UserResponse.builder().createdBy("SYSTEM").createdOn(LocalDateTime.now()).id(3L).emailAddress("test3@test.com").firstName("Junit3").lastName("Test3").username("junit3").updatedBy("SYSTEM").updatedOn(LocalDateTime.now()).roles(Set.of("SUPER_ADMIN")).build(),
    			UserResponse.builder().createdBy("SYSTEM").createdOn(LocalDateTime.now()).id(4L).emailAddress("test4@test.com").firstName("Junit4").lastName("Test4").username("junit4").updatedBy("SYSTEM").updatedOn(LocalDateTime.now()).roles(Set.of("SUPER_ADMIN")).build(),
    			UserResponse.builder().createdBy("SYSTEM").createdOn(LocalDateTime.now()).id(5L).emailAddress("test5@test.com").firstName("Junit5").lastName("Test5").username("junit5").updatedBy("SYSTEM").updatedOn(LocalDateTime.now()).roles(Set.of("SUPER_ADMIN")).build(),
    			UserResponse.builder().createdBy("SYSTEM").createdOn(LocalDateTime.now()).id(6L).emailAddress("test6@test.com").firstName("Junit6").lastName("Test6").username("junit6").updatedBy("SYSTEM").updatedOn(LocalDateTime.now()).roles(Set.of("SUPER_ADMIN")).build()
    	);
    }
}
