package com.universum.service.label.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateLanguageRequest extends LanguageRequest {
	private static final long serialVersionUID = 5132733132938794736L;
	@NotBlank(message = "Language code cannot be null or empty.")
	private String code;
}
