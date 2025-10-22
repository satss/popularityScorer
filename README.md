# PopularityScoringService
The purpose of this application is to fetch repos via github search api and assign popularity score calculated based on 
few factors like stars, forks and recentlyUpdateAt( aka pushedAt)

## Pre-requisites
* docker
* docker-compose
* java 21 jdk

## Running the application
* Run `docker-compose up -d` to start the mongodb instance
    * The mongodb instance will be initialized with a demo database and credentials via the `mongodb-init/init.js` script
* Run `mvn spring-boot:run` to start the application

## Testing the application
The integration test **[PopularityScorerIntegrationTests.java](src/test/java/com/example/popularityScorer/PopularityScorerIntegrationTests.java))** runs with the help of MongoDB Testcontainers (https://testcontainers.com/).
For them to run, you need to have docker installed and running on your machine.

# Design and Architecture
see [Solution](docs/Solution.md)
