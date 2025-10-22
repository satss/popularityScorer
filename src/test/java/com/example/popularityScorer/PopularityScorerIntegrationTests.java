package com.example.popularityScorer;


import com.example.popularityScorer.github.GithubRepoService;
import com.example.popularityScorer.github.Language;
import com.example.popularityScorer.github.SearchService;
import com.example.popularityScorer.github.score.PopularityScoreCalculator;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.example.popularityScorer.testConfig.MongoDBTestContainerConfig;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertThrows;


@Testcontainers
@ContextConfiguration(
        classes = MongoDBTestContainerConfig.class
)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@EnableWireMock({
        @ConfigureWireMock(
                port = 9561)
})
class PopularityScorerIntegrationTests {

    @InjectWireMock
    WireMockServer wireMock;
    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private SearchService searchService;

    @Autowired
    private GithubRepoService githubRepoService;

    @Autowired
    private PopularityScoreCalculator popularityScoreCalculator;

    @BeforeEach
    public void setUp() {
        mongoOperations.getCollectionNames().forEach(it ->
                mongoOperations.getCollection(it).deleteMany(new Document())
        );

        mongoOperations.dropCollection("fetchUntil");
        mongoOperations.dropCollection("githubRepos");
    }

    @Test
    public void shouldBeAbleToFetchGithubRepositor() {
        getGithubSearchApiResponse();
        searchService.fetchGithubRepos();
        Assertions.assertTrue(mongoOperations.collectionExists("fetchUntil"));
        Assertions.assertTrue(mongoOperations.collectionExists("githubRepos"));

    }

    @Test
    public void shouldBeAbleToCheckPopularityScoreIsNotNull() throws InterruptedException {
        getGithubSearchApiResponse();
        searchService.fetchGithubRepos();
        Thread.sleep(1_000);
        popularityScoreCalculator.assignScore();

        var pageableGithubRepos =githubRepoService.getAllPopularRepos(null,
                PageRequest.of(0, 30,
                (Sort.by("createdAt").descending())));

        pageableGithubRepos.getContent().forEach(it -> {
            Assertions.assertNotNull(it.getPopularityScore());
        });
    }

    @Test
    public void shouldBeAbleToCheckPythonAsLanguageWhenReposExists() throws InterruptedException {
        getGithubSearchApiResponse();
        searchService.fetchGithubRepos();
        Thread.sleep(1_000);
        popularityScoreCalculator.assignScore();

        var pageableGithubRepos =githubRepoService.getAllPopularRepos(null,
                PageRequest.of(0, 30,
                        (Sort.by("repoCreatedAt").descending())));

        Assertions.assertEquals(Language.Python, pageableGithubRepos.getContent().getFirst().getLanguage());
    }

    @Test
    public void noFurtherActionIsRequiredWhenSizeIsEmptyItem() throws InterruptedException {

       var githubRepoBeforeInvocation =
               githubRepoService.getAllPopularRepos(null,
                PageRequest.of(0, 30,
                        (Sort.by("repoCreatedAt").descending())));
        getEmptyGithubSearchApiResponse();
        searchService.fetchGithubRepos();
        Thread.sleep(1_000);


        var pageableGithubRepos =githubRepoService.getAllPopularRepos(null,
                PageRequest.of(0, 30,
                        (Sort.by("repoCreatedAt").descending())));

        Assertions.assertEquals(githubRepoBeforeInvocation.getContent().size(), pageableGithubRepos.getContent().size());
    }

    @Test
    public void shouldThrowExceptionWhenGithubServerRespondsWithAnyError() {

        getErrorFromGithubSearchApiResponse();
        assertThrows(RuntimeException.class, () ->
                searchService.fetchGithubRepos());
    }

    @Test
    public void shouldBeAbleToUpdatePopularityScore() throws InterruptedException {
        getGithubSearchApiResponse();
        searchService.fetchGithubRepos();
        Thread.sleep(1_000);
        popularityScoreCalculator.assignScore();


        var pageableGithubRepos =githubRepoService.getAllPopularRepos(null,
                PageRequest.of(0, 30,
                        (Sort.by("repoCreatedAt").descending())));
        var firstContent = pageableGithubRepos.getContent().getFirst();

        var gitHubId = firstContent.getGithubId();
        githubRepoService.assignScore(gitHubId,Double.valueOf("30.0"));

        var refetchRepo =githubRepoService.getAllPopularRepos(null,
                PageRequest.of(0, 30,
                        (Sort.by("repoCreatedAt").descending()))).getContent().getFirst();

        Assertions.assertEquals(refetchRepo.getPopularityScore(),Double.valueOf("30.0"));
    }

    void getEmptyGithubSearchApiResponse() {
        wireMock.stubFor(get("/search/repositories?q=Q&page=1&per_page=30")
                .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {"total_count": 0,
                                "incomplete_results": 0,
                                "items" : []}
    
                                """)));

    }

    void getErrorFromGithubSearchApiResponse() {
        wireMock.stubFor(get("/search/repositories?q=Q&page=1&per_page=30")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(422)
                        .withBody("""
                                {}
                                """)));

    }

    void getGithubSearchApiResponse() {
        wireMock.stubFor(get("/search/repositories?q=Q&page=1&per_page=30")
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                           {
                                                     "total_count": 2,
                                                     "incomplete_results": false,
                                                     "items": [
                                                       {
                                                         "id": 1,
                                                         "name": "q",
                                                         "full_name": "kriskowal/q",
                                                         "private": false,
                                                         "html_url": "https://github.com/kriskowal/q",
                                                         "created_at": "2010-09-04T01:21:12Z",
                                                         "updated_at": "2025-10-22T06:25:24Z",
                                                         "pushed_at": "2023-11-08T10:50:34Z",
                                                         "stargazers_count": 15202,
                                                         "watchers_count": 15202,
                                                         "language": "JavaScript",
                                                         "forks_count": 1188,
                                                         "visibility": "public",
                                                         "forks": 1188,
                                                         "watchers": 15202,
                                                         "score": 1.0
                                                       },
                                                       {
                                                         "id": 2,
                                                         "name": "q",
                                                         "full_name": "harelba/q",
                                                         "private": false,
                                                         "html_url": "https://github.com/harelba/q",
                                                         "created_at": "2012-01-30T21:12:09Z",
                                                         "updated_at": "2025-10-22T03:36:46Z",
                                                         "pushed_at": "2025-05-27T21:40:50Z",
                                                         "stargazers_count": 10321,
                                                         "watchers_count": 10321,
                                                         "language": "Python",
                                                         "forks_count": 426,
                                                         "visibility": "public",
                                                         "forks": 426,
                                                         "watchers": 10321,
                                                         "score": 1.0
                                                       }
                                                     ]
                                                   }
                                """)
                        .withStatus(200)));
    }

}
