package com.example.popularityScorer.api.model;

import lombok.Builder;

import java.util.List;

@Builder
public record PaginatedPopularRepo(List<ApiPopularRepoV1> paginatedPopularRepo,
                                   Integer totalPages,
                                   Integer currentPage,
                                   Long totalItems) {
}
