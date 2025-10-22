package com.example.popularityScorer.github;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "fetchUntil")
@TypeAlias("fetchUntil")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchTracker {

    @Id
    String id;

    Long totalCount;

    Long pageCountedUntil;

    Long maxPageNumber;

    @Indexed(background = true)
    LocalDateTime lastFetchedAt;

    @Version
    private Long version;

   SearchTracker merge(SearchTracker currentSearchTracker) {
       this.totalCount = currentSearchTracker.totalCount;
       this.pageCountedUntil = currentSearchTracker.pageCountedUntil;
       this.maxPageNumber = currentSearchTracker.maxPageNumber;
       this.lastFetchedAt = currentSearchTracker.lastFetchedAt;
       return this;
    }

}
