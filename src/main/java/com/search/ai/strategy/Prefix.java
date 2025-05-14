package com.search.ai.strategy;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.PrefixQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.search.ai.annotations.StrategyType;
import com.search.ai.constants.SearchStrategy;
import com.search.ai.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.search.ai.constants.Index.INDEX_NAME;

@Component
@StrategyType(SearchStrategy.PREFIX)
public class Prefix implements Strategy {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Override
    public List<?> search(String keyword) {
        System.out.println("Inside Prefix text search");
        Query query = PrefixQuery.of(p -> p
                .field("title")
                .value(keyword.toLowerCase())
        )._toQuery();

        SearchRequest searchRequest = SearchRequest.of(s -> s
                .index(INDEX_NAME)
                .query(query)
                .size(5) // limit results
        );

        SearchResponse<Note> response = null;
        try {
            response = elasticsearchClient.search(searchRequest, Note.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response.hits().hits().stream()
                .map(Hit::source)
                .filter(note -> note != null)
//                .map(Note::getTitle)
                .collect(Collectors.toList());
    }
}
