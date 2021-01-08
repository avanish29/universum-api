package com.universum.common.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PageResponse<T> implements Serializable {
	private static final long serialVersionUID = 309219557584034694L;
	private long totalItems;
    private long totalPages;
    private long currentPage;
    private List<T> contents;
}
