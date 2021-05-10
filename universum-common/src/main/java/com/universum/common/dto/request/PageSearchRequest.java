package com.universum.common.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;

@Data
public final class PageSearchRequest {
	@JsonAlias({"q", "Q"})
	private String query = StringUtils.EMPTY; // Default to empty
	
	@Min(value = 0, message = "Paging must start with page 1")
	private int page = 0; // Default to first page
	
	@Min(value = 1, message = "You can request minimum 1 records")
    @Max(value = 100, message = "You can request maximum 100 records")
	private int size = 10; // Default to 10 rows per page
	
	private String sortBy = "id";// Default to primary key 'ID'
	
	private String order = "ASC"; // Default to ASCENDING order
	
	public PageSearchRequest() {}
	
	public PageSearchRequest(int page, int size) {
		this.page = page;
		this.size = size;
	}

	private Sort.Direction sortDirection() {
		Optional<Sort.Direction> direction = Sort.Direction.fromOptionalString(this.getOrder());
		return direction.orElse(Sort.Direction.ASC);
	}

	public Pageable pageable() {
		return PageRequest.of(this.getPage(), this.getSize(), Sort.by(new Sort.Order(sortDirection(), sortBy).ignoreCase()));
	}
}
