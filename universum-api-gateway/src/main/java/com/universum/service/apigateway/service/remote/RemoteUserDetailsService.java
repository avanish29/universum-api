package com.universum.service.apigateway.service.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.universum.service.apigateway.dto.LoggedInUser;

@FeignClient(name = "security-service")
public interface RemoteUserDetailsService {
	@GetMapping(value = "/users/{username}", produces = "application/json")
    LoggedInUser getByUserName(@PathVariable("username") String userName);
}
