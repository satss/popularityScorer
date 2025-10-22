package com.example.popularityScorer.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PopularRepoRequest(@JsonProperty("github_id") String githubId,
                                 @JsonProperty("score") Double score) {
}
