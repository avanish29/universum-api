package com.universum.service.security.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
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

import com.universum.common.exception.NotFoundException;
import com.universum.common.model.UniversumPageRequest;
import com.universum.common.model.UniversumPageResponse;
import com.universum.security.util.AuthenticationConstant;
import com.universum.service.security.dto.CreateUserRequest;
import com.universum.service.security.dto.UpdateUserRequest;
import com.universum.service.security.dto.UserResponse;
import com.universum.service.security.entity.ApplicationRole;
import com.universum.service.security.entity.ApplicationUser;
import com.universum.service.security.repository.AppRoleRepository;
import com.universum.service.security.repository.AppUserRepository;
import com.universum.service.security.util.AuthenticationEventType;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class AppUserService implements UserDetailsService {
	@Autowired
	private AppUserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AppRoleRepository roleRepository;
	
	@Transactional(readOnly = true)
	@PreAuthorize("hasAnyRole('" + AuthenticationConstant.SYSTEM_ADMIN + "', '" + AuthenticationConstant.SUPER_ADMIN + "')")
	public UniversumPageResponse<UserResponse> getAllUsers(@NonNull final UniversumPageRequest appPageRequest) {
		Optional<Direction> sortDir = Direction.fromOptionalString(appPageRequest.getOrder());
		PageRequest pageRequest = PageRequest.of(appPageRequest.getOffset(), appPageRequest.getLimit(), sortDir.isPresent() ? sortDir.get() : Direction.ASC , StringUtils.isBlank(appPageRequest.getSort()) ? "id" : appPageRequest.getSort());
        
		Page<ApplicationUser> pageResponse = userRepository.findAllByDeletedFalse(pageRequest);
		List<UserResponse> usersResponse = Optional.of(pageResponse.getContent()).orElseGet(Collections::emptyList).parallelStream().map(applicationUser -> UserResponse.fromEntity(applicationUser, false)).collect(Collectors.toList());
        
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
				userRepository.findOneWithRolesByUsernameAndDeletedFalse(userName)
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
				
		ApplicationUser appUser = new ApplicationUser();
		appUser.setEmailAddress(userRequest.getEmail());
		appUser.setFirstName(userRequest.getFirstName());
		appUser.setLastName(userRequest.getLastName());
		appUser.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));
		appUser.setUsername(userRequest.getUsername());
		Set<ApplicationRole> managedRoles = new HashSet<>();
		userRequest.getRoles().parallelStream()
									.map(roleRepository::findOneByNameAndDeletedFalse)
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
		Optional<ApplicationUser> updatedUser = Optional.of(this.findOneWithRolesByIdOrNotFound(userId))
				.map(appUser -> {
					appUser.setFirstName(updateRequest.getFirstName());
					appUser.setLastName(updateRequest.getLastName());
					appUser.setEmailAddress(updateRequest.getEmail());
					Set<ApplicationRole> managedRoles = appUser.getRoles();
					managedRoles.clear();
					updateRequest.getRoles().parallelStream()
								 .map(roleRepository::findOneByNameAndDeletedFalse)
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
		Optional<ApplicationUser> deletedUser = Optional.of(this.findOneWithRolesByIdOrNotFound(userId))
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
	
	public UserResponse onAuthenticationEvent(@NonNull final String userName, final LocalDateTime timestamp, final AuthenticationEventType eventType) {
		Optional<ApplicationUser> updatedUser = Optional.of(userRepository.findOneByUsername(userName))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(appUser -> {
					if(AuthenticationEventType.AUTHENTICATION_SUCCESS == eventType) {
						appUser.setLastSuccessfulLoginTime(timestamp);
						appUser.setFailedLoginAttempts(0);
					}
					if(AuthenticationEventType.AUTHENTICATION_FAILURE == eventType) {
						appUser.setLastLoginFailureTime(timestamp);
						appUser.setFailedLoginAttempts(ObjectUtils.defaultIfNull(appUser.getFailedLoginAttempts(), 0) + 1);
					}
					appUser = userRepository.save(appUser);
					log.debug("Update user on authentication success : {}", appUser);
					return appUser;
				});
		return UserResponse.fromEntity(updatedUser.isPresent() ? updatedUser.get() : null, false);
	}
	
	ApplicationUser findOneWithRolesByIdOrNotFound(final Long id) {
		Optional<ApplicationUser> userById = userRepository.findOneWithRolesById(id);
		if(userById.isEmpty() || BooleanUtils.isTrue(userById.get().getDeleted())) {
			throw new NotFoundException(ApplicationUser.class, id);
		}
		return userById.get();
	}
}
