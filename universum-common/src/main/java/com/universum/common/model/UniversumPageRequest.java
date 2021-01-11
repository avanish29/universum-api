package com.universum.common.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.Getter;

@Data
public final class UniversumPageRequest {
	@NotBlank
	private String query = StringUtils.EMPTY;
	@Min(0)
	private int offset = 0;
	@Min(1)
	private int limit = 10;
	@NotBlank
	private String sort;
	@NotBlank
	private String order;
	
	private UniversumPageRequest() {}
}
