package com.universum.common.dto.response;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageSearchResponse<D> {
	private long totalItems;
    private long totalPages;
    private long currentPage;
    @ToString.Exclude
    private List<D> contents;
    
    /**
	 * Creates a new {@link PageSearchResponse} with page result and converter
	 * function.
	 * 
	 * @param pageResult        - Paged result from database.
	 * @param converterFunction - {@link Function} to convert entity to model
	 * @param <D>               - Model type
	 * @param <E>               - Entity type
	 * @return - Instance of {@link PageSearchResponse}
	 */
	public static <D, E> PageSearchResponse<D> of(final Page<E> pageResult, final Function<E, D> converterFunction) {
		List<D> contents = pageResult.stream().map(converterFunction).collect(Collectors.toList());
		return new PageSearchResponse<>(pageResult.getTotalElements(), pageResult.getTotalPages(), pageResult.getNumber(), contents);
	}

	/**
	 * Create a new {@Link PageSearchResponse} with page contents, totalItems, totalPages and currentPage.
	 * @param totalItems - Total number of records.
	 * @param totalPages - total number of pages.
	 * @param currentPage - current page number.
	 * @param contents - List of response object.
	 * @param <D> - Response type.
	 * @return - Instance of {@link PageSearchResponse}
	 */
	public static <D> PageSearchResponse<D> of(final long totalItems, final long totalPages, final long currentPage, final List<D> contents) {
		return new PageSearchResponse<>(totalItems, totalPages, currentPage, contents);
	}
}
