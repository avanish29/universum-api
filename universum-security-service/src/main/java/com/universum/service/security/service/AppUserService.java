package com.universum.service.security.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.universum.common.model.UniversumPageRequest;
import com.universum.common.model.UniversumPageResponse;
import com.universum.service.security.dto.UserResponse;
import com.universum.service.security.entity.ApplicationUser;
import com.universum.service.security.repository.AppUserRepository;

import lombok.NonNull;

@Service
public class AppUserService implements UserDetailsService {
	@Autowired
	private AppUserRepository userRepository;
	
	public UniversumPageResponse<UserResponse> getAllUsers(@NonNull final UniversumPageRequest appPageRequest) {
		Optional<Direction> sortDir = Direction.fromOptionalString(appPageRequest.getOrder());
		PageRequest pageRequest = PageRequest.of(appPageRequest.getOffset(), appPageRequest.getLimit(), sortDir.isPresent() ? sortDir.get() : Direction.ASC , StringUtils.isBlank(appPageRequest.getSort()) ? "id" : appPageRequest.getSort());
        
		Page<ApplicationUser> pageResponse = userRepository.findAllByDeletedFalse(pageRequest);
		List<UserResponse> usersResponse = Optional.of(pageResponse.getContent()).orElseGet(Collections::emptyList).stream().map(UserResponse::fromEntity).collect(Collectors.toList());
        
        return new UniversumPageResponse<>(pageResponse.getTotalElements(), pageResponse.getTotalPages(), pageResponse.getNumber(), usersResponse);
	}
	
	public UserResponse findUserById(@NonNull final Long id) {
		return UserResponse.fromEntity(userRepository.findByIdNotDeleted(id));
	}
	
	@Override
	public UserDetails loadUserByUsername(final String userName) throws UsernameNotFoundException {
		return UserResponse.fromEntity(userRepository.findByUsernameAndDeletedFalse(userName).orElseThrow(() -> new UsernameNotFoundException(String.format("User with username '%s' does not exists!", userName))));
	}
}
