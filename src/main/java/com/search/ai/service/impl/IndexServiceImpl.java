package com.search.ai.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.search.ai.service.IndexService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static com.search.ai.constants.Index.INDEX_NAME;

public class IndexServiceImpl implements IndexService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Override
    public void createIndexWithCustomAnalyzer() {
        CreateIndexRequest request = new CreateIndexRequest.Builder()
                .index(INDEX_NAME)
                .settings(s -> s
                        .analysis(a -> a
                                .analyzer("custom_analyzer", ca -> ca
                                        .custom(c -> c
                                                .tokenizer("standard")
                                                .filter("lowercase", "stop", "porter_stem")
                                        )
                                )
                        )
                )
                .mappings(m -> m
                        .properties("title", p -> p.text(t -> t.analyzer("custom_analyzer")))
                        .properties("content", p -> p.text(t -> t.analyzer("custom_analyzer")))
                )
                .build();

        try {
            CreateIndexResponse response = elasticsearchClient.indices().create(request);
            if (response.acknowledged()) {
                System.out.println("Index created with custom analyzer.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create index", e);
        }
    }

    @PostConstruct
    @Override
    public void recreateIndex() {
        try {
            // Check if index exists
            boolean exists = elasticsearchClient.indices().exists(e -> e.index(INDEX_NAME)).value();

            // Delete if exists
            if (exists) {
                elasticsearchClient.indices().delete(d -> d.index(INDEX_NAME));
                System.out.println("Deleted existing index: " + INDEX_NAME);
            }

            // Create new index
            CreateIndexRequest request = new CreateIndexRequest.Builder()
                    .index(INDEX_NAME)
                    .settings(s -> s
                            .analysis(a -> a
                                    .analyzer("custom_analyzer", ca -> ca
                                            .custom(c -> c
                                                    .tokenizer("standard")
                                                    .filter("lowercase", "stop", "porter_stem")
                                            )
                                    )
                            )
                    )
                    .mappings(m -> m
                            .properties("title", p -> p.text(t -> t.analyzer("custom_analyzer")))
                            .properties("content", p -> p.text(t -> t.analyzer("custom_analyzer")))
                    )
                    .build();

            CreateIndexResponse response = elasticsearchClient.indices().create(request);

            if (response.acknowledged()) {
                System.out.println("Recreated index '" + INDEX_NAME + "' with custom analyzer.");
            } else {
                System.out.println("Index creation not acknowledged.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to recreate index", e);
        }
    }
}
