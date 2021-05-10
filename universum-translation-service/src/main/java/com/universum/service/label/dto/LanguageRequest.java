package com.universum.service.label.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import com.universum.common.validator.constraint.ValidEnum;
import com.universum.service.label.util.LanguageDirection;

import lombok.Data;

@Data
public class LanguageRequest implements Serializable {
	private static final long serialVersionUID = 1061456791228308281L;

	@ValidEnum(enumClass = LanguageDirection.class, message = "Language direction must be either 'LTR' or 'RTL'.")
	private String dir;
	
	private Boolean isDefault = Boolean.FALSE;
	
	@NotBlank(message = "Language label cannot be null or empty.")
	private String label;
}
