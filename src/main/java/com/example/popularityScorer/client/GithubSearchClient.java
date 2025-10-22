package com.example.popularityScorer.client;

import com.example.popularityScorer.github.RollingWindowThrottler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@Slf4j
@Component
public class GithubSearchClient extends HttpBaseClient {


    private final ObjectMapper objectMapper;

    private final RollingWindowThrottler rollingWindowThrottler;

    private final HttpClient httpClient;

    @Autowired
    public GithubSearchClient(ObjectMapper objectMapper,
                              RollingWindowThrottler rollingWindowThrottler,
                              HttpClient httpClient) {
        this.objectMapper = objectMapper;
        this.rollingWindowThrottler = rollingWindowThrottler;
        this.httpClient = httpClient;
    }

    public GithubSearchResponse getRepos(Long pageNumber, Long pageSize) {
        var httpRequest =
                buildHttRequestWithBaseURL(
                        "/search/repositories?q=Q&page="+pageNumber+"&per_page="+pageSize);
        try {
            rollingWindowThrottler.next();
           var response =  httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
           if(response.statusCode() == 200) {
               return objectMapper.readValue(response.body(), GithubSearchResponse.class);
           }else {
               log.error("Github search returned status code other than 200 which is {}", response.statusCode());
              throw new IllegalAccessException("Currently something went wrong while searching github repositories");
           }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
