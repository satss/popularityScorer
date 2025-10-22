package com.example.popularityScorer;


import com.example.popularityScorer.github.SearchService;
import com.github.tomakehurst.wiremock.WireMockServer;
import integrationtest.testConfig.MongoDBTestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;


@Testcontainers
@ContextConfiguration(
        classes = MongoDBTestContainerConfig.class
)
@SpringBootTest(
webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class PopularityScorerApplicationTests {


    @Autowired
    private MongoOperations mongoOperations;

    @Autowired
    private SearchService searchService;

    @Test
    public void fetchRepository() {
        WireMockServer wireMockServer = new WireMockServer(9561);
        wireMockServer.start();

        wireMockServer.stubFor(get(urlPathMatching("/search/repositories?"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                   {
                                             "total_count": 3455499,
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

  searchService.fetchGithubRepos();

  wireMockServer.stop();


    }


}
