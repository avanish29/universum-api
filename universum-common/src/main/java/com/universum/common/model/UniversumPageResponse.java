package com.universum.common.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class UniversumPageResponse<T> {
	private long totalItems;
    private long totalPages;
    private long currentPage;
    private List<T> contents;
}
