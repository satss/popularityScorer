package com.example.popularityScorer.github;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GithubRepoRepository extends MongoRepository<GithubRepo, String> {

    Optional<GithubRepo> findGithubRepoByGithubId(String id);

    List<GithubRepo> findGithubRepoByPopularityScoreIsNull();

    @Query("{language : ?0}")
    Page<GithubRepo> findGithubRepoByPopularityScore(Language language, Pageable pageable);
}
