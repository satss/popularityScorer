package com.example.popularityScorer.github;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SearchTrackerRepo extends MongoRepository<SearchTracker, String> {
   Optional<SearchTracker> findTopByOrderByLastFetchedAtDesc();
}
