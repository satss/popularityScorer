package com.example.popularityScorer.testConfig;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;

@TestConfiguration
public class MongoDBTestContainerConfig{

    @Bean
    @ServiceConnection
    protected MongoDBContainer mongoDBContainer() {
        return new MongoDBContainer("mongo:6.0.13");
    }

}
