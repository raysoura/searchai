package com.search.ai.strategy;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.search.ai.annotations.StrategyType;
import com.search.ai.constants.SearchStrategy;
import com.search.ai.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@StrategyType(SearchStrategy.FULL_TEXT)
public class FullTextSearch implements Strategy {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Override
    public List<?> search(String keyword) {
        System.out.println("Inside Full text search");
        try {
            // Build a multi_match query with fuzziness and boost
            Query query = MultiMatchQuery.of(q -> q
                    .fields("title^1.2", "content")
                    .query(keyword)
                    .fuzziness("AUTO")
            )._toQuery();

            // Send the search request
            SearchResponse<Note> response = elasticsearchClient.search(s -> s
                            .index("notes")  // Make sure your index name matches
                            .query(query)
                            .from(0)
                            .size(10),
                    Note.class
            );

            // Map hits to Note objects
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .filter(Objects::nonNull)
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Failed to search notes", e);
        }
    }
}
