package com.example.popularityScorer.scheduler;

import com.example.popularityScorer.github.SearchService;
import com.example.popularityScorer.github.score.PopularityScoreCalculator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private final PopularityScoreCalculator popularityScoreCalculator;
    private final SearchService searchService;

    public Scheduler(PopularityScoreCalculator popularityScoreCalculator,
                     SearchService searchService) {
        this.popularityScoreCalculator = popularityScoreCalculator;
        this.searchService = searchService;
    }
//* 5 * * * *
    @Scheduled(cron = "* * * * * *", zone = "UTC")
    public void fetchRepositories() {
          searchService.fetchGithubRepos();
    }

    //* /56 * * * *
    @Scheduled(cron = "* * * * * *", zone = "UTC")
    public void assignPopularityScores() {
        popularityScoreCalculator.assignScore();
    }
}
