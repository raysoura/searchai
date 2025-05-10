package com.search.ai.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.PrefixQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.search.ai.repository.NoteRepository;
import com.search.ai.model.Note;
import com.search.ai.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.search.ai.constants.Index.INDEX_NAME;


@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepository repository;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Override
    public Note save(Note note) {
        return repository.save(note);
    }

    @Override
    public Optional<Note> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public Note update(Note note) {
        return repository.save(note);
    }

    @Override
    public List<Note> searchByText(String keyword) {
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

    @Override
    public List<Note> getAllNotes() {
        Iterable<Note> notesIterable = repository.findAll();
        return StreamSupport.stream(notesIterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> autocomplete(String prefix) {
        Query query = PrefixQuery.of(p -> p
                .field("title")
                .value(prefix.toLowerCase())
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
                .map(Note::getTitle)
                .collect(Collectors.toList());
    }


}
