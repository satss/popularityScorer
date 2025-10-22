package com.example.popularityScorer.github;


import com.example.popularityScorer.client.GithubSearchResponse;
import com.example.popularityScorer.github.score.PopularityScoreCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GithubRepoService {

    private final GithubRepoRepository githubRepoRepository;

    private final PopularityScoreCalculator popularityScoreCalc;

    private final MongoTemplate mongoTemplate;

    public GithubRepoService(GithubRepoRepository githubRepoRepository,
                             PopularityScoreCalculator popularityScoreCalc, MongoTemplate mongoTemplate) {
        this.githubRepoRepository = githubRepoRepository;
        this.popularityScoreCalc = popularityScoreCalc;
        this.mongoTemplate = mongoTemplate;
    }

    public void createGithubItem(GithubSearchResponse.GithubItem githubSearchItem) {

        var optionalGithubRepo = githubRepoRepository.findGithubRepoByGithubId(githubSearchItem.id());
        var githubRepo = GithubRepo.builder()
                .githubId(githubSearchItem.id())
                .forks(githubSearchItem.forks())
                .stars(githubSearchItem.stars())
                .watchers(githubSearchItem.watchers())
                .name(githubSearchItem.name())
                .language(Language.fromString(githubSearchItem.language()))
                .githubUrl(githubSearchItem.githubUrl())
                .repoLastPushedAt(githubSearchItem.pushedAt().toLocalDateTime())
                .repoCreatedAt(githubSearchItem.createdAt().toLocalDateTime())
                .repoLastUpdatedAt(githubSearchItem.updatedAt().toLocalDateTime())
                .build();

        if(optionalGithubRepo.isEmpty()) {
            githubRepoRepository.save(githubRepo);
            return;
        }

        var existingRepo = optionalGithubRepo.get();

        var isSame = existingRepo.isSame(githubRepo);
        if(!isSame) {
            log.info("A github item already exists");
            updateGithubItem(existingRepo, githubRepo);
        }
    }

    public void updateGithubItem(GithubRepo existingGithubRepo,
                                 GithubRepo currentGithubRepo) {
        var mergedGithubRepo = existingGithubRepo.merge(currentGithubRepo);
        var score =
                popularityScoreCalc.calculatePopularityScore(mergedGithubRepo);
        mergedGithubRepo.setPopularityScore(score);
        githubRepoRepository.save(mergedGithubRepo);

    }

    public Page<GithubRepo> findPopularScore(String language,
                                             Pageable pageable) {
        if(language == null) {
            return githubRepoRepository.findAll(pageable);
        }
       return githubRepoRepository.findGithubRepoByPopularityScore(Language.fromString(language),pageable);

    }

    public void assignScore(String githubId, Double score) {
       var existingGithubRepo =  githubRepoRepository.findGithubRepoByGithubId(githubId);
       if(existingGithubRepo.isEmpty()) {
           throw new IllegalArgumentException("Github Repo not found for id: " + githubId);
       }
       existingGithubRepo.get().setPopularityScore(score);
       githubRepoRepository.save(existingGithubRepo.get());
    }
}
