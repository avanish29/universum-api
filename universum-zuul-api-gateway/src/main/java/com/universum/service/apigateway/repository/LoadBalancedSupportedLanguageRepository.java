package com.universum.service.apigateway.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import com.universum.common.model.UniversumPageResponse;
import com.universum.service.apigateway.dto.AvailableLanguage;

@Repository
@FeignClient(name = "label-service")
public interface LoadBalancedSupportedLanguageRepository {
	@GetMapping(value = "languages?query=name:abc&sort=id&order=asc&offset=0&limit=100", produces = "application/json")
	UniversumPageResponse<AvailableLanguage> getAllSuppotedLaguages();
}
