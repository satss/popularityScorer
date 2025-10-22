package com.example.popularityScorer.api;

import lombok.Builder;

import java.util.List;

@Builder
public record PaginatedPopularRepo(List<ApiPopularRepo> paginatedPopularRepo,
                                   Integer totalPages,
                                   Integer currentPage,
                                   Long totalItems) {
}
