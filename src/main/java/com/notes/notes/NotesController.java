package com.notes.notes;

import Model.Note;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class NotesController {

    private List<Note> notes = new ArrayList<>();

    @GetMapping("/notes")
    public List<Note> getNotes() {
        return notes;
    }

    @GetMapping("/notes/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable int id) {
        for (Note note : notes) {
            if (note.getId() == id) {
                return ResponseEntity.ok(note);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/notes")
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        int newId = notes.size() + 1;
        note.setId(newId);
        notes.add(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(note);
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable int id, @RequestBody Note updatedNote) {
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            if (note.getId() == id) {
                note.setTitle(updatedNote.getTitle());
                note.setContent(updatedNote.getContent());
                return ResponseEntity.ok(note);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable int id) {
        notes.removeIf(note -> note.getId() == id);
        return ResponseEntity.noContent().build();
    }
}
