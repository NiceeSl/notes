package com.notes.notes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/notes")
public class NotesController {
    private final NoteService noteService;
    private static final Logger logger = LoggerFactory.getLogger(NotesController.class);

    @Autowired
    public NotesController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<NoteDto> getAllNotes() {
        List<NoteDto> notes = noteService.getAllNotes();
        logger.info("Retrieved {} notes", notes.size());
        return notes;
    }


    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> getNoteById(@PathVariable Long id) {
        NoteDto noteDto = noteService.getNoteById(id);
        if (noteDto != null) {
            return ResponseEntity.ok(noteDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<NoteDto> createNote(@RequestBody NoteDto noteDto) {
        NoteDto createdNoteDto = noteService.createNote(noteDto);
        return ResponseEntity.ok(createdNoteDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDto> updateNote(@PathVariable Long id, @RequestBody NoteDto noteDto) {
        NoteDto updatedNoteDto = noteService.updateNote(id, noteDto);
        if (updatedNoteDto != null) {
            return ResponseEntity.ok(updatedNoteDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {
        noteService.deleteNoteById(id);
    }
}
