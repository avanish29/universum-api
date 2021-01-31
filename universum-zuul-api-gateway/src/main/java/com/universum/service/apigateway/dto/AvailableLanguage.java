package com.universum.service.apigateway.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvailableLanguage implements Serializable {
	private static final long serialVersionUID = -1232248747995138130L;
	private String code;
	private String dir;
	private Boolean isDefault;
	private String label;
}
