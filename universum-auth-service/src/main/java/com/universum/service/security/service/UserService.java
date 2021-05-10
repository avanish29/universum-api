/**
 * Copyright (c) 2021-present Universum Systems. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.universum.service.security.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.universum.common.dto.request.PageSearchRequest;
import com.universum.common.dto.response.PageSearchResponse;
import com.universum.common.exception.NotFoundException;
import com.universum.security.util.AuthenticationConstant;
import com.universum.service.security.domain.Role;
import com.universum.service.security.domain.User;
import com.universum.service.security.dto.request.CreateUserRequest;
import com.universum.service.security.dto.request.UpdateUserRequest;
import com.universum.service.security.dto.response.UserResponse;
import com.universum.service.security.listener.AuthenticationAuditListener;
import com.universum.service.security.repository.RoleRepository;
import com.universum.service.security.repository.UserRepository;
import com.universum.service.security.service.mapper.UserMapper;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for managing users.
 * @author Avanish
 */
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
	
	@Autowired
	private UserMapper userMapper;
	
	/**
	 * Returns a {@link PageSearchResponse} of {@link UserResponse} meeting the paging restriction provided in the {@code PageSearchRequest} object.
	 * 
	 * @param pageSearchRequest - Instance of {@link PageSearchRequest} contains restrictions used to construct criteria query
	 * @return - Instance of {@link PageSearchResponse} contains page result of {@link UserResponse}
	 */
	@Transactional(readOnly = true)
	@PreAuthorize("hasAnyRole('" + AuthenticationConstant.SYSTEM_ADMIN + "', '" + AuthenticationConstant.SUPER_ADMIN + "')")
	public PageSearchResponse<UserResponse> getAllUsers(@NonNull final PageSearchRequest pageSearchRequest) {
		log.info("Find all users request {}", pageSearchRequest);
		Page<User> pageResult = userRepository.findAll(pageSearchRequest.pageable());
		PageSearchResponse<UserResponse> pageSearchResponse = PageSearchResponse.of(pageResult, userMapper::toResponse);
		log.info("Find all users response {}", pageSearchResponse);
        return pageSearchResponse;
	}
	
	/** 
	 * Return an instance of {@link UserResponse} by {@literal User.id}
	 * 
	 * @param id - Id must not be {@literal null}.
	 * @return - The instance of {@link UserResponse} with the given id or {@literal NotFoundException} if none found.
	 */
	@Transactional(readOnly = true)
	@PreAuthorize("hasAnyRole('" + AuthenticationConstant.SYSTEM_ADMIN + "', '" + AuthenticationConstant.SUPER_ADMIN + "')")
	public UserResponse findUserById(@NonNull final Long id) {
		return UserResponse.fromEntity(this.findOneWithRolesByIdOrNotFound(id), true);
	}
	
	/**
	 *  Locates the user based on the UserName.
	 *  
	 *  @param userName - UserName the UserName identifying the user whose data is required.
	 *  @return -  A fully populated user record (never <code>null</code>)
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
		return UserResponse.fromEntity(
				userRepository.findOneWithRolesByUsername(userName)
				.orElseThrow(() -> new UsernameNotFoundException(String.format("User with username '%s' does not exists!", userName))), true);
	}
	
	/**
	 * Create user and return the newly created user.
	 * 
	 * @param userRequest - instance of {@link CreateUserRequest} containing information about the user to be created.
	 * @return - An instance of newly created user {@link UserResponse}. Throw {@link ValidationException} if user with UserName already present.
	 */
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
				
		var appUser = userMapper.toEntity(userRequest, roleRepository, passwordEncoder);
		appUser = userRepository.save(appUser);
		log.debug("Created User with information: {}", appUser);
		return UserResponse.fromEntity(appUser, true);
	}
	
	/**
	 * Update user and return updated instance.
	 * 
	 * @param userId - Id of the user whose data need to update.
	 * @param updateRequest - Instance of {@link UpdateUserRequest} contains data which need to be update
	 * @return - An instance of updated user {@link UserResponse}; will throw an exception {@link NotFoundException} if user with id does not exist.
	 */
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
	
	/**
	 * Soft delete user from database and return instance of deleted user.
	 * 
	 * @param userId - Id of the user whose need to be delete.
	 * @return An instance of deleted user {@link UserResponse}; will throw an exception {@link NotFoundException} if user with id does not exist.
	 */
	@PreAuthorize("hasAnyRole('" + AuthenticationConstant.SYSTEM_ADMIN + "', '" + AuthenticationConstant.SUPER_ADMIN + "')")
	public UserResponse deleteUser(@NonNull final Long userId) {
		Optional<User> deletedUser = Optional.of(this.findOneWithRolesByIdOrNotFound(userId))
				.map(appUser -> {
					appUser.setUsername(String.format("_%s_%s", userId, appUser.getUsername()));
					appUser.setDeleted(Boolean.TRUE);
					appUser.setActive(Boolean.FALSE);
					appUser = userRepository.save(appUser);
					log.debug("Deleted User: {}", appUser);
					return appUser;
				});
		return UserResponse.fromEntity(deletedUser.orElse(null), false);
	}
	
	/**
	 * Handle event of {@link AuthenticationAuditListener}. 
	 * On {@link AuthenticationEventType.AUTHENTICATION_SUCCESS} application update the lastSuccessfulLoginTime to {@linkplain timestamp} and failedLoginAttempts to zero.
	 * On {@link AuthenticationEventType.AUTHENTICATION_FAILURE} application update the lastLoginFailureTime to {@linkplain timestamp} and failedLoginAttempts to failedLoginAttempts + 1.
	 * 
	 * @param userName - UserName identifying the user whose login is either SUCCESS or FAILURE is required.
	 * @param timestamp - Time when the event occurred.
	 * @param eventType - Type of event.
	 */
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
	
	/**
	 * Locates the user with roles based on the id.
	 * 
	 * @param id - Id of the user.
	 * @return - An instance of user {@link User}; will throw an exception {@link NotFoundException} if user with id does not exist.
	 */
	User findOneWithRolesByIdOrNotFound(final Long id) {
		return userRepository.findOneWithRolesById(id).orElseThrow(() -> new NotFoundException(User.class, id));
	}
}
