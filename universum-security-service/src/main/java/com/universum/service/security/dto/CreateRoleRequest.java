package com.universum.service.security.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CreateRoleRequest {
	@NotBlank()
	@Size(max = 255)
	@Pattern(regexp = "^[a-zA-Z0-9]([a-zA-Z0-9_]*[a-zA-Z0-9])?$")
    private String name;
	
	@Size(max = 255)
	private String description;
}
