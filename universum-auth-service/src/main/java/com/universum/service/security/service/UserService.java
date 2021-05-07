package com.universum.service.security.service;

import com.universum.common.exception.NotFoundException;
import com.universum.common.model.UniversumPageRequest;
import com.universum.common.model.UniversumPageResponse;
import com.universum.security.util.AuthenticationConstant;
import com.universum.service.security.domain.Role;
import com.universum.service.security.domain.User;
import com.universum.service.security.dto.request.CreateUserRequest;
import com.universum.service.security.dto.request.UpdateUserRequest;
import com.universum.service.security.dto.response.UserResponse;
import com.universum.service.security.listener.AuthenticationAuditListener;
import com.universum.service.security.repository.RoleRepository;
import com.universum.service.security.repository.UserRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class UserService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Transactional(readOnly = true)
	@PreAuthorize("hasAnyRole('" + AuthenticationConstant.SYSTEM_ADMIN + "', '" + AuthenticationConstant.SUPER_ADMIN + "')")
	public UniversumPageResponse<UserResponse> getAllUsers(@NonNull final UniversumPageRequest appPageRequest) {
		Optional<Direction> sortDir = Direction.fromOptionalString(appPageRequest.getOrder());
		var pageRequest = PageRequest.of(appPageRequest.getOffset(), appPageRequest.getLimit(), sortDir.isPresent() ? sortDir.get() : Direction.ASC , StringUtils.isBlank(appPageRequest.getSort()) ? "id" : appPageRequest.getSort());
        
		Page<User> pageResponse = userRepository.findAll(pageRequest);
		List<UserResponse> usersResponse = Optional.of(pageResponse.getContent()).orElseGet(Collections::emptyList).parallelStream().map(user -> UserResponse.fromEntity(user, false)).collect(Collectors.toList());
        
        return new UniversumPageResponse<>(pageResponse.getTotalElements(), pageResponse.getTotalPages(), pageResponse.getNumber(), usersResponse);
	}
	
	@Transactional(readOnly = true)
	@PreAuthorize("hasAnyRole('" + AuthenticationConstant.SYSTEM_ADMIN + "', '" + AuthenticationConstant.SUPER_ADMIN + "')")
	public UserResponse findUserById(@NonNull final Long id) {
		return UserResponse.fromEntity(this.findOneWithRolesByIdOrNotFound(id), true);
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
		return UserResponse.fromEntity(
				userRepository.findOneWithRolesByUsername(userName)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("User with username '%s' does not exists!", userName))), true);
	}
	
	@PreAuthorize("hasAnyRole('" + AuthenticationConstant.SYSTEM_ADMIN + "', '" + AuthenticationConstant.SUPER_ADMIN + "')")
	public UserResponse createUser(@NonNull final CreateUserRequest userRequest) {
		if(!userRequest.getPassword().equals(userRequest.getRePassword())) {
			throw new ValidationException("Passwords don't match!");
		}
		
		Optional.of(userRequest.getUsername())
				.map(userRepository::countByUsername)
				.filter(count -> count >= 1)
				.ifPresent(count -> {
					throw new ValidationException(String.format("User with username '%s' does not exists!", userRequest.getUsername()));
				});
				
		User appUser = new User();
		appUser.setEmailAddress(userRequest.getEmail());
		appUser.setFirstName(userRequest.getFirstName());
		appUser.setLastName(userRequest.getLastName());
		appUser.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));
		appUser.setUsername(userRequest.getUsername());
		Set<Role> managedRoles = new HashSet<>();
		userRequest.getRoles().parallelStream()
									.map(roleRepository::findOneByName)
									.filter(Optional::isPresent)
									.map(Optional::get)
									.forEach(managedRoles::add);
		appUser.setRoles(managedRoles);
		appUser = userRepository.save(appUser);
		log.debug("Created User with information: {}", appUser);
		return UserResponse.fromEntity(appUser, true);
	}
	
	@PreAuthorize("hasAnyRole('" + AuthenticationConstant.SYSTEM_ADMIN + "', '" + AuthenticationConstant.SUPER_ADMIN + "')")
	public UserResponse updateUser(@NonNull final Long userId, @NonNull final UpdateUserRequest updateRequest) {
		Optional<User> updatedUser = Optional.of(this.findOneWithRolesByIdOrNotFound(userId))
				.map(appUser -> {
					appUser.setFirstName(updateRequest.getFirstName());
					appUser.setLastName(updateRequest.getLastName());
					appUser.setEmailAddress(updateRequest.getEmail());
					Set<Role> managedRoles = appUser.getRoles();
					managedRoles.clear();
					updateRequest.getRoles().parallelStream()
								 .map(roleRepository::findOneByName)
								 .filter(Optional::isPresent)
								 .map(Optional::get)
								 .forEach(managedRoles::add);
					appUser = userRepository.save(appUser);
					log.debug("Changed Information for User: {}", appUser);
					return appUser;
				});
		return UserResponse.fromEntity(updatedUser.isPresent() ? updatedUser.get() : null, true);
	}
	
	@PreAuthorize("hasAnyRole('" + AuthenticationConstant.SYSTEM_ADMIN + "', '" + AuthenticationConstant.SUPER_ADMIN + "')")
	public UserResponse deleteUser(@NonNull final Long userId) {
		Optional<User> deletedUser = Optional.of(this.findOneWithRolesByIdOrNotFound(userId))
				.map(appUser -> {
					appUser.setUsername(String.format("_%s_%s", String.valueOf(userId), appUser.getUsername()));
					appUser.setDeleted(Boolean.TRUE);
					appUser.setActive(Boolean.FALSE);
					appUser = userRepository.save(appUser);
					log.debug("Deleted User: {}", appUser);
					return appUser;
				});
		return UserResponse.fromEntity(deletedUser.isPresent() ? deletedUser.get() : null, false);
	}
	
	public void onAuthenticationEvent(@NonNull final String userName, final LocalDateTime timestamp, final AuthenticationAuditListener.AuthenticationEventType eventType) {
		log.debug("Updating user : {} for authentication event type : {}", userName, eventType.name());
		var updateCount = 0;
		if(AuthenticationAuditListener.AuthenticationEventType.AUTHENTICATION_SUCCESS == eventType) {
			updateCount = userRepository.updateOnLoginSuccess(timestamp, 0, userName);
		} else if(AuthenticationAuditListener.AuthenticationEventType.AUTHENTICATION_FAILURE == eventType) {
			updateCount = userRepository.updateOnLoginFailure(timestamp, userName);
		}
		log.debug("{} user updated for authentication event type : {}", updateCount, eventType.name());
	}
	
	User findOneWithRolesByIdOrNotFound(final Long id) {
		return userRepository.findOneWithRolesById(id).orElseThrow(() -> new NotFoundException(User.class, id));
	}
}
