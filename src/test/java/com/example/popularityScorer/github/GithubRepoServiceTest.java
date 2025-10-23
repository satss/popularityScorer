package com.example.popularityScorer.github;

import com.example.popularityScorer.client.GithubSearchResponse;
import com.example.popularityScorer.github.score.PopularityScoreCalculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubRepoServiceTest {

    @Mock
    GithubRepoRepository githubRepoRepository;

    @Mock
    PopularityScoreCalculator popularityScoreCalculator;

    @InjectMocks
    GithubRepoService githubRepoService;

    @Test
    public void shouldCreateNewGithubItemWhenIdDoesNotExists() {

        when(githubRepoRepository.findGithubRepoByGithubId(any())).thenReturn(Optional.empty());
        when(githubRepoRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        var githubRepo = GithubSearchResponse.GithubItem.builder().githubUrl("https://github.com")
                .createdAt(OffsetDateTime.now())
                .id(UUID.randomUUID().toString())
                .updatedAt(OffsetDateTime.now())
                .pushedAt(OffsetDateTime.now())
                .name("name")
                .stars(20L)
                .forks(30L).language(Language.Uncategorized.name())
                .watchers(30L).build();

        var response = githubRepoService.createGithubItem(githubRepo);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(githubRepo.id(), response.githubId);
        Assertions.assertEquals(githubRepo.githubUrl(), response.githubUrl);
    }


    @Test
    public void shouldUpdateScoreWhenScoreIndicatorsAreChanged() {

        GithubRepo githubRepo = GithubRepo.builder()
                .id("1")
                .githubId("url.com")
                .repoCreatedAt(OffsetDateTime.parse("2010-09-04T01:21:12Z").toLocalDateTime())
                .repoLastPushedAt(OffsetDateTime.parse("2025-10-22T06:25:24Z").toLocalDateTime())
                .repoLastUpdatedAt(OffsetDateTime.parse("2023-11-08T10:50:34Z").toLocalDateTime())
                .name("name")
                .stars(15202L)
                .watchers(15202L)
                .popularityScore(10.0)
                .language(Language.Python)
                .forks(1188L)
                .build();

        when(githubRepoRepository.findGithubRepoByGithubId(any())).thenReturn(Optional.of(githubRepo));
        when(githubRepoRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        var githubItem = GithubSearchResponse.GithubItem.builder().githubUrl("https://github.com")
                .id(UUID.randomUUID().toString())
                .updatedAt(OffsetDateTime.parse("2025-10-22T03:36:46Z"))
                .pushedAt(OffsetDateTime.parse("2025-05-27T21:40:50Z"))
                .createdAt(OffsetDateTime.parse("2010-09-04T01:21:12Z"))
                .name("name")
                .stars(10321L)
                .forks(1188L).language(Language.Uncategorized.name())
                .watchers(10321L).build();

        when(popularityScoreCalculator.calculatePopularityScore(any())).thenCallRealMethod();

        var response = githubRepoService.createGithubItem(githubItem);
        Assertions.assertNotNull(response);

        Assertions.assertNotEquals(Math.ceil(githubRepo.popularityScore), response.popularityScore);


    }

}