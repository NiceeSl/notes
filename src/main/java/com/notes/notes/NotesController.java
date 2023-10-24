package com.notes.notes;
import Model.Note;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

@RestController
public class NotesController {

    @GetMapping("/notes")
    public List<Note> getNotes() {
        return Arrays.asList(
                new Note(1, "Первая заметка", "Содержание первой заметки"),
                new Note(2, "Вторая заметка", "Содержание второй заметки")
        );
    }
}