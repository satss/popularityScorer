package com.example.popularityScorer.github;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

@Document(collection = "githubRepos")
@TypeAlias("githubRepo")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubRepo {
    @Id
    String id;

    @Indexed(unique = true)
    String githubId;

    String name;

    String githubUrl;

    Long stars;

    Long watchers;

    Long forks;

    @Indexed(background = true)
    LocalDateTime repoLastUpdatedAt;

    @Indexed(background = true)
    LocalDateTime repoCreatedAt;

    @Indexed(background = true)
    LocalDateTime repoLastPushedAt;

    @Version
    private Long version;

    Language language;

    Double popularityScore = null;

    LocalDateTime createdAt = LocalDateTime.now();

    Boolean isSame(GithubRepo newRepo) {
        return Objects.equals(this.forks, newRepo.forks) && Objects.equals(this.stars, newRepo.stars)
                && this.repoLastUpdatedAt == newRepo.repoLastUpdatedAt;
    }

    GithubRepo merge(GithubRepo newRepo) {
        this.forks = newRepo.forks;
        this.stars = newRepo.stars;
        this.repoLastUpdatedAt = newRepo.repoLastUpdatedAt;
        return this;
    }
}
