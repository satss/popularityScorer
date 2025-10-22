package com.example.popularityScorer.github;

import com.example.popularityScorer.client.GithubSearchClient;
import com.example.popularityScorer.client.GithubSearchResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
    @Mock
    SearchTrackerRepo searchTrackerRepo;

    @Mock
    GithubSearchClient githubSearchClient;

    @Mock
    GithubRepoRepository githubRepoRepository;

    @Mock
    GithubRepoService githubRepoService;

    @InjectMocks
    SearchService searchService;

    @Test
    public void shouldDoNothingWithNoResponseIsReceived() {
        when(searchTrackerRepo.findTopByOrderByLastFetchedAtDesc()).thenReturn(Optional.empty());
        when(githubSearchClient.getRepos(anyLong(), anyLong())).thenReturn(
                new GithubSearchResponse(List.of(), 0L, "false"));

        searchService.fetchGithubRepos();

        verify(searchTrackerRepo, times(0)).save(any());
        verify(githubRepoService, times(0)).createGithubItem(any());

    }

    @Test
    public void shouldDoNothingWithNoResponseIsReceived2() {
        when(searchTrackerRepo.findTopByOrderByLastFetchedAtDesc()).thenReturn(Optional.empty());

        GithubSearchResponse.GithubItem githubSearchItem = GithubSearchResponse.GithubItem.builder()
                .id("1")
                .stars(2L)
                .watchers(2L)
                .forks(3L)
                .githubUrl("some url")
                .name("some name")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .pushedAt(OffsetDateTime.now())
                .language("Java")
                .build();

        when(githubSearchClient
                .getRepos(anyLong(),
                        anyLong()))
                .thenReturn(new GithubSearchResponse(List.of(githubSearchItem),
                        0L,
                        "false"));

        when(searchTrackerRepo.save(any())).thenAnswer(args -> args.getArguments()[0]);

        Mockito.lenient().when(githubRepoRepository.findGithubRepoByGithubId(any(String.class))).thenReturn(Optional.empty());

        Mockito.lenient().when(githubRepoRepository.save(any())).thenAnswer(args -> args.getArguments()[0]);

        searchService.fetchGithubRepos();

        verify(githubRepoService, times(1)).createGithubItem(any());
    }

}