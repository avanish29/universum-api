package com.universum.service.apigateway.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.universum.service.apigateway.dto.AvailableLanguage;
import com.universum.service.apigateway.repository.LoadBalancedSupportedLanguageRepository;

@Service
public class LoadBalancedSupportedLanguageService {
	@Autowired
	private LoadBalancedSupportedLanguageRepository supportedLanguageRepository;
	
	public List<AvailableLanguage> getAllSuppotedLaguages() {
		return supportedLanguageRepository.getAllSuppotedLaguages();
	}
}
