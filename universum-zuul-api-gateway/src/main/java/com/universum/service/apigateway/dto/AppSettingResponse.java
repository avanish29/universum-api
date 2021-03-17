package com.universum.service.apigateway.dto;

import java.util.List;

import lombok.Data;

@Data
public class AppSettingResponse {
	private String theme = "light";
	private String menuPosition = "side";
	private String headerPos = "fixed";
	private String formFieldAppearance = "standard";
	private String loginType = "default";
	private boolean registrationEnabled = true;
	private List<AvailableLanguage> supportedLanguages;
}
