package com.example.popularityScorer.api.model;

import java.time.LocalDateTime;

public record ApiPopularRepoV1(String githubId,
                               String name,
                               String language,
                               Double popularityScore,
                               LocalDateTime repoCreatedAt) {
}
