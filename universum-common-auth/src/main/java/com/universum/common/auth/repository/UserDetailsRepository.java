package com.universum.common.auth.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.universum.common.auth.dto.UserDetailsResponse;

@FeignClient(name = "security-service")
public interface UserDetailsRepository {
	@GetMapping(value = "/users/{username}", produces = "application/json")
	UserDetailsResponse getByUserName(@PathVariable("username") String userName);
}
