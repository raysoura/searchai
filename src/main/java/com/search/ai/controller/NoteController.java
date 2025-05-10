package com.search.ai.controller;

import com.search.ai.model.Note;
import com.search.ai.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping("/create")
    public Note createNote(@RequestBody Note note) {
        return noteService.save(note);
    }

    @PutMapping("/update/{id}")
    public Note updateNote(@PathVariable String id, @RequestBody Note note) {
        note.setId(id);
        return noteService.update(note);
    }

    @GetMapping("/get/{id}")
    public Optional<Note> getNote(@PathVariable String id) {
        return noteService.findById(id);
    }

    @GetMapping("/search")
    public List<Note> searchNotes(@RequestParam String keyword) {
        return noteService.searchByText(keyword);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Note>> getAllNotes() {
        List<Note> notes = noteService.getAllNotes();
        if (notes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/search/autocomplete")
    public ResponseEntity<List<String>> autocomplete(@RequestParam String prefix) {
        List<String> suggestions = noteService.autocomplete(prefix);
        if (suggestions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(suggestions);

    }
}
