package com.search.ai.repository;

import com.search.ai.model.Note;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends ElasticsearchRepository<Note, String> {
}
