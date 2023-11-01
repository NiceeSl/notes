package com.notes.notes;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NotesController {
    private final NoteService noteService;


    @Autowired
    public NotesController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<NoteDto> getAllNotes() {
        return noteService.getAllNotes();
    }


    @GetMapping("/{id}")
    public ResponseEntity<NoteDto> getNoteById(@PathVariable Long id) throws NotFoundException {
        NoteDto noteDto = noteService.getNoteById(id);
        if (noteDto != null) {
            return ResponseEntity.ok(noteDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<NoteDto> createNote(@RequestBody NoteDto noteDto) throws NotFoundException {
        NoteDto createdNoteDto = noteService.createNote(noteDto);
        return ResponseEntity.ok(createdNoteDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDto> updateNote(@PathVariable Long id, @RequestBody NoteDto noteDto) throws NotFoundException {
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
