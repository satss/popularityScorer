package com.example.popularityScorer.client;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.net.URI;
import java.net.http.HttpRequest;

@Getter
@RequiredArgsConstructor
public abstract class HttpBaseClient {

    @Value("${GITHUB_BASE_URL}")
    private String githubBaseUrl;

    HttpRequest buildHttRequestWithBaseURL(
            @NonNull String endpoint){

        return HttpRequest.newBuilder()
                .uri(URI.create(githubBaseUrl + endpoint))
                .header("content-type", MediaType.APPLICATION_JSON.toString())
                .header("X-GitHub-Api-Version", "2022-11-28")
                .method(
                        HttpMethod.GET.name(), HttpRequest.BodyPublishers.noBody())
                .build();
    }
}
