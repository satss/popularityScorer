package com.example.popularityScorer.github.score;

import com.example.popularityScorer.github.GithubRepoRepository;
import com.example.popularityScorer.github.GithubRepo;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class PopularityScoreCalculator {
    private final static Double GRAVITY = 1.8;

    private final GithubRepoRepository githubRepoRepository;

    public PopularityScoreCalculator(GithubRepoRepository githubRepoRepository) {
        this.githubRepoRepository = githubRepoRepository;
    }

    public void assignScore() {
        var unscoredLists =
                githubRepoRepository.findGithubRepoByPopularityScoreIsNull();
        var popularityScoreUpdated = new ArrayList<GithubRepo>();

        for (GithubRepo repo : unscoredLists) {
            var score = calculatePopularityScore(repo);
            repo.setPopularityScore(score);
            popularityScoreUpdated.add(repo);
        }
        githubRepoRepository.saveAll(popularityScoreUpdated);
    }

    public Double calculatePopularityScore(GithubRepo githubRepo) {
        var numerator = (githubRepo.getStars() - 1) + (githubRepo.getForks() == null ? 0 : githubRepo.getForks());
        var repoHourAge = Duration.between(
                githubRepo.getRepoLastPushedAt(), LocalDateTime.now()).toHours();
        return (numerator / Math.pow((repoHourAge + 2), GRAVITY));
    }


}
