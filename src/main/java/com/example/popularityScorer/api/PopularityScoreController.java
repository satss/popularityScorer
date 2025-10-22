package com.example.popularityScorer.api;


import com.example.popularityScorer.api.model.ApiPopularRepoV1;
import com.example.popularityScorer.api.model.PaginatedPopularRepo;
import com.example.popularityScorer.api.model.PopularRepoRequest;
import com.example.popularityScorer.github.GithubRepo;
import com.example.popularityScorer.github.GithubRepoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/repos")
public class PopularityScoreController {

    private final GithubRepoService githubRepoService;

    public PopularityScoreController(GithubRepoService githubRepoService) {
        this.githubRepoService = githubRepoService;
    }

    @GetMapping()
    public PaginatedPopularRepo getPopularRepos(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size,
            @RequestParam(defaultValue = "repoCreatedAt") String sortBy,
            @RequestParam(name = "language", required = false) String language
    ) {
        Pageable pageable = PageRequest.of(page, size, (Sort.by(sortBy).descending()));
        return toPaginatedApi(githubRepoService.getAllPopularRepos(language, pageable));
    }

    @PutMapping()
    public void assignScoreToAnExistingRepo(
            @RequestBody PopularRepoRequest popularRepoRequest
    ) {
        githubRepoService.assignScore(popularRepoRequest.githubId(), popularRepoRequest.score());
    }

    private PaginatedPopularRepo toPaginatedApi(Page<GithubRepo> githubRepos) {
        var popularRepos = githubRepos.getContent().stream().map(repo ->
                new ApiPopularRepoV1(repo.getGithubId(),
                        repo.getName(), repo.getLanguage().name(), repo.getPopularityScore(),
                        repo.getRepoCreatedAt())).toList();
        return PaginatedPopularRepo.builder().paginatedPopularRepo(popularRepos)
                .totalPages(githubRepos.getTotalPages())
                .currentPage(githubRepos.getNumber())
                .totalItems(githubRepos.getTotalElements()).build();
    }


}


