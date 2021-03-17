package com.universum.common.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public final class UniversumPageRequest {
	private String query = StringUtils.EMPTY;
	
	@Min(value = 0, message = "Paging must start with page 1")
	private int offset = 0;
	
	@Min(value = 1, message = "You can request minimum 1 records")
    @Max(value = 100, message = "You can request maximum 100 records")
	private int limit = 10;
	
	private String sort;
	
	private String order;
	
	private UniversumPageRequest() {}
}
