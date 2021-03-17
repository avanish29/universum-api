package com.universum.service.apigateway.repository;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import com.universum.service.apigateway.dto.AvailableLanguage;

@Repository
@FeignClient(name = "label-service")
public interface LoadBalancedSupportedLanguageRepository {
	@GetMapping(value = "languages", produces = "application/json")
	List<AvailableLanguage> getAllSuppotedLaguages();
}
