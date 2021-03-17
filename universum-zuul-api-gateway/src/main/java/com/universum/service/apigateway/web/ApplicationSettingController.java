package com.universum.service.apigateway.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.universum.service.apigateway.dto.AppSettingResponse;
import com.universum.service.apigateway.service.LoadBalancedSupportedLanguageService;

@RestController
@RequestMapping("/settings")
public class ApplicationSettingController {
	
	@Autowired
	private LoadBalancedSupportedLanguageService supportedLanguageService;
	
	@GetMapping
	public AppSettingResponse getAppSetting() {
		AppSettingResponse settingResponse = new AppSettingResponse();
		settingResponse.setSupportedLanguages(supportedLanguageService.getAllSuppotedLaguages());
		return settingResponse;
	}
}
