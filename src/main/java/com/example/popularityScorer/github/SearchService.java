package com.example.popularityScorer.github;

import com.example.popularityScorer.client.GithubSearchClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class SearchService {

    private final SearchTrackerRepo searchTrackerRepo;
    private final GithubSearchClient githubSearchClient;

    private static final Long pageSize = 30L;

    private final GithubRepoService githubRepoService;

    public SearchService(SearchTrackerRepo searchTrackerRepo,
                         GithubSearchClient githubSearchClient,
                         GithubRepoService githubRepoService) {
        this.searchTrackerRepo = searchTrackerRepo;
        this.githubSearchClient = githubSearchClient;
        this.githubRepoService = githubRepoService;
    }

    public void fetchGithubRepos() {
        var fetchedUntil = searchTrackerRepo.findTopByOrderByLastFetchedAtDesc();
        if (fetchedUntil.isEmpty()) {
            var searchResults = githubSearchClient.getRepos(1L, pageSize);
            if (!searchResults.items().isEmpty()) {
                searchTrackerRepo.save(SearchTracker.builder().totalCount(searchResults.totalCount())
                        .lastFetchedAt(LocalDateTime.now())
                        .pageCountedUntil(1L)
                        .lastFetchedAt(LocalDateTime.now())
                        .maxPageNumber((searchResults.totalCount() + pageSize - 1) / pageSize)
                        .build());
            }
            searchResults.items().forEach(githubRepoService::createGithubItem);
            return;
        }
        long pageNumber = fetchedUntil.get().pageCountedUntil + 1;

        if (pageNumber > fetchedUntil.get().maxPageNumber) {
            log.info("Nothing more to fetch");
            return;
        }
        var searchResults = githubSearchClient.getRepos(pageNumber, pageSize);

        long maxPageNumber = (searchResults.totalCount() + pageSize - 1) / pageSize;

        var githubSearchTracker = SearchTracker.builder().totalCount(searchResults.totalCount())
                .lastFetchedAt(LocalDateTime.now())
                .pageCountedUntil(pageNumber)
                .maxPageNumber(maxPageNumber)
                .build();

        searchTrackerRepo.save(fetchedUntil.get().merge(githubSearchTracker));
        searchResults.items().forEach(githubRepoService::createGithubItem);
    }
}
