package com.universum.service.apigateway.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.universum.service.apigateway.dto.LoggedInUser;

@Repository
@FeignClient(name = "security-service")
public interface LoadBalancedUserDetailsRepository {
	@GetMapping(value = "/users/{username}", produces = "application/json")
    LoggedInUser getByUserName(@PathVariable("username") String userName);
}
