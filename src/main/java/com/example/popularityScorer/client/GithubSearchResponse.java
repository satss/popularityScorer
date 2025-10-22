package com.example.popularityScorer.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.time.OffsetDateTime;
import java.util.List;


public record GithubSearchResponse(@JsonProperty("items") List<GithubItem> items,
                                   @JsonProperty("total_count") Long totalCount,
                                   @JsonProperty("incompleteResults") String incompleteResults) {

    @Builder
    public record GithubItem(@JsonProperty("id") String id,
                             @JsonProperty("name")String name,
                             @JsonProperty("stargazers_count") Long stars,
                             @JsonProperty("watchers_count") Long watchers,
                             @JsonProperty("created_at") OffsetDateTime createdAt,
                             @JsonProperty("updated_at")OffsetDateTime updatedAt,
                             @JsonProperty("pushed_at")OffsetDateTime pushedAt,
                             @JsonProperty("language")String language,
                             @JsonProperty("forks_count")Long forks,
                             @JsonProperty("html_url")String githubUrl) {

    }


}


