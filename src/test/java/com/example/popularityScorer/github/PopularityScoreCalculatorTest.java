package com.example.popularityScorer.github;

import com.example.popularityScorer.github.score.PopularityScoreCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class PopularityScoreCalculatorTest {

    @Mock
    GithubRepoRepository githubRepoRepository;

    @InjectMocks
    PopularityScoreCalculator popularityScoreCalculator;

    @Test
    void shouldHaveMorePopularityScoreSinceHasMoreStars() {

        GithubRepo andrejKarapathyRepo = GithubRepo.builder()
                .id(UUID.randomUUID().toString())
                .githubUrl("nanoChatGpt")
                .stars(40000L)
                .forks(56666L)
                .language(Language.Python)
                .repoCreatedAt(LocalDateTime.now().minusDays(365))
                .repoLastUpdatedAt(LocalDateTime.now().minusDays(365))
                .repoLastPushedAt(LocalDateTime.now().minusDays(1))
                .watchers(453453L)
                .githubId(UUID.randomUUID().toString())
                .name("AndrejKarapathy")
                .build();

        GithubRepo samAltmanRepo = GithubRepo.builder()
                .id(UUID.randomUUID().toString())
                .githubUrl("openAiChanges")
                .stars(20000L)
                .forks(56666L)
                .language(Language.Python)
                .repoCreatedAt(LocalDateTime.now().minusDays(1))
                .repoLastUpdatedAt(LocalDateTime.now().minusDays(1))
                .repoLastPushedAt(LocalDateTime.now().minusDays(1))
                .watchers(453453L)
                .githubId(UUID.randomUUID().toString())
                .name("Sam Altman")
                .build();


        var scoreForAndrejKarapathy = popularityScoreCalculator.calculatePopularityScore(andrejKarapathyRepo);
        var scoreForSamAltman = popularityScoreCalculator.calculatePopularityScore(samAltmanRepo);
        assert (scoreForAndrejKarapathy > scoreForSamAltman);

    }


    @Test
    void shouldHaveLessPopularityScoreSinceHasMoreStars() {

        GithubRepo andrejKarapathyRepo = GithubRepo.builder()
                .id(UUID.randomUUID().toString())
                .githubUrl("nanoChatGpt")
                .stars(400L)
                .forks(400L)
                .language(Language.Python)
                .repoCreatedAt(LocalDateTime.now().minusDays(6))
                .repoLastUpdatedAt(LocalDateTime.now().minusDays(6))
                .repoLastPushedAt(LocalDateTime.now().minusDays(1))
                .watchers(453453L)
                .githubId(UUID.randomUUID().toString())
                .name("AndrejKarapathy")
                .build();

        GithubRepo samAltmanRepo = GithubRepo.builder()
                .id(UUID.randomUUID().toString())
                .githubUrl("openAiChanges")
                .stars(350L)
                .forks(600L)
                .language(Language.Python)
                .repoCreatedAt(LocalDateTime.now().minusDays(8))
                .repoLastUpdatedAt(LocalDateTime.now().minusDays(9))
                .repoLastPushedAt(LocalDateTime.now().minusDays(1))
                .watchers(453453L)
                .githubId(UUID.randomUUID().toString())
                .name("Sam Altman")
                .build();


        var scoreForAndrejKarapathy = popularityScoreCalculator.calculatePopularityScore(andrejKarapathyRepo);
        var scoreForSamAltman = popularityScoreCalculator.calculatePopularityScore(samAltmanRepo);
        assert (scoreForAndrejKarapathy < scoreForSamAltman);

    }

    @Test
    void shouldHaveLessPopularityScoreSinceNoLatestUpdates() {

        GithubRepo andrejKarapathyRepo = GithubRepo.builder()
                .id(UUID.randomUUID().toString())
                .githubUrl("nanoChatGpt")
                .stars(400L)
                .forks(400L)
                .language(Language.Python)
                .repoCreatedAt(LocalDateTime.now().minusDays(3658))
                .repoLastUpdatedAt(LocalDateTime.now().minusDays(3658))
                .repoLastPushedAt(LocalDateTime.now().minusDays(3658))
                .watchers(453453L)
                .githubId(UUID.randomUUID().toString())
                .name("AndrejKarapathy")
                .build();

        GithubRepo samAltmanRepo = GithubRepo.builder()
                .id(UUID.randomUUID().toString())
                .githubUrl("openAiChanges")
                .stars(400L)
                .forks(400L)
                .language(Language.Python)
                .repoCreatedAt(LocalDateTime.now().minusDays(8))
                .repoLastUpdatedAt(LocalDateTime.now().minusDays(9))
                .repoLastPushedAt(LocalDateTime.now().minusDays(1))
                .watchers(453453L)
                .githubId(UUID.randomUUID().toString())
                .name("Sam Altman")
                .build();


        var scoreForAndrejKarapathy = popularityScoreCalculator.calculatePopularityScore(andrejKarapathyRepo);
        var scoreForSamAltman = popularityScoreCalculator.calculatePopularityScore(samAltmanRepo);
        assert (scoreForAndrejKarapathy < scoreForSamAltman);

    }

}