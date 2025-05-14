package com.search.ai.service;

import com.search.ai.constants.SearchStrategy;
import com.search.ai.model.Note;

import java.util.List;
import java.util.Optional;

public interface NoteService {

    Note save(Note note);

    Optional<Note> findById(String id);

    Note update(Note note);

    List<Note> searchByText(String keyword);

    List<Note> getAllNotes();

    List<String> autocomplete(String prefix);

    List<?> search(String keyword, SearchStrategy strategy);
}
