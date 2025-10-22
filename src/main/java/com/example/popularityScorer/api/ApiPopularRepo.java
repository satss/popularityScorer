package com.example.popularityScorer.api;

import java.time.LocalDateTime;

public record ApiPopularRepo(String githubId,
                             String name,
                             String language,
                             Double popularityScore,
                             LocalDateTime repoCreatedAt) {
}
