package com.universum.common.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class UniversumPageResponse<T> implements Serializable {
	private static final long serialVersionUID = 309219557584034694L;
	private long totalItems;
    private long totalPages;
    private long currentPage;
    private List<T> contents;
}
