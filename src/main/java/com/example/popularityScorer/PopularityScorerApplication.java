package com.example.popularityScorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.http.HttpClient;

@SpringBootApplication
@EnableScheduling
public class PopularityScorerApplication {

	public static void main(String[] args) {
        SpringApplication.run(PopularityScorerApplication.class, args);
	}

    @Bean
    public HttpClient buildHttpClient() {
        return HttpClient.newHttpClient();
    }

}
