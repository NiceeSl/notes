package com.notes.notes;

import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotesControllerTest {

    @Mock
    private NoteService noteService;

    @InjectMocks
    private NotesController notesController;

    @Test
    public void testGetAllNotes() {
        // Подготовка данных для теста
        List<NoteDto> mockNotes = new ArrayList<>();
        mockNotes.add(new NoteDto());
        mockNotes.add(new NoteDto());

        when(noteService.getAllNotes()).thenReturn(mockNotes);

        // Вызываем метод контроллера
        List<NoteDto> result = notesController.getAllNotes();

        // Проверяем, что метод noteService.getAllNotes() был вызван
        verify(noteService, times(1)).getAllNotes();

        // Проверяем, что результат соответствует ожиданиям
        assertEquals(mockNotes.size(), result.size());
    }

    @Test
    public void testGetNoteById_WhenNoteExists() throws NotFoundException {
        // Подготовка данных для теста
        Long noteId = 1L;
        NoteDto mockNote = new NoteDto();
        mockNote.setId(noteId);

        when(noteService.getNoteById(noteId)).thenReturn(mockNote);

        // Вызываем метод контроллера
        ResponseEntity<NoteDto> result = notesController.getNoteById(noteId);

        // Проверяем, что метод noteService.getNoteById() был вызван
        verify(noteService, times(1)).getNoteById(noteId);

        // Проверяем, что результат соответствует ожиданиям
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(mockNote, result.getBody());
    }

    @Test
    public void testGetNoteById_WhenNoteDoesNotExist() throws NotFoundException {
        // Подготовка данных для теста
        Long noteId = 1L;
        when(noteService.getNoteById(noteId)).thenReturn(null);

        // Вызываем метод контроллера
        ResponseEntity<NoteDto> result = notesController.getNoteById(noteId);

        // Проверяем, что метод noteService.getNoteById() был вызван
        verify(noteService, times(1)).getNoteById(noteId);

        // Проверяем, что результат соответствует ожиданиям
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testCreateNote() throws NotFoundException {
        // Подготовка данных для теста
        NoteDto mockNote = new NoteDto();

        when(noteService.createNote(any())).thenReturn(mockNote);

        // Вызываем метод контроллера
        ResponseEntity<NoteDto> result = notesController.createNote(mockNote);

        // Проверяем, что метод noteService.createNote() был вызван
        verify(noteService, times(1)).createNote(any());

        // Проверяем, что результат соответствует ожиданиям
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(mockNote, result.getBody());
    }

    @Test
    public void testUpdateNote_WhenNoteExists() throws NotFoundException {
        // Подготовка данных для теста
        Long noteId = 1L;
        NoteDto mockNote = new NoteDto();
        mockNote.setId(noteId);

        when(noteService.updateNote(eq(noteId), any())).thenReturn(mockNote);

        // Вызываем метод контроллера
        ResponseEntity<NoteDto> result = notesController.updateNote(noteId, mockNote);

        // Проверяем, что метод noteService.updateNote() был вызван
        verify(noteService, times(1)).updateNote(eq(noteId), any());

        // Проверяем, что результат соответствует ожиданиям
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(mockNote, result.getBody());
    }

    @Test
    public void testUpdateNote_WhenNoteDoesNotExist() throws NotFoundException {
        // Подготовка данных для теста
        Long noteId = 1L;

        when(noteService.updateNote(eq(noteId), any())).thenReturn(null);

        // Вызываем метод контроллера
        ResponseEntity<NoteDto> result = notesController.updateNote(noteId, new NoteDto());

        // Проверяем, что метод noteService.updateNote() был вызван
        verify(noteService, times(1)).updateNote(eq(noteId), any());

        // Проверяем, что результат соответствует ожиданиям
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void testDeleteNote() {
        // Вызываем метод контроллера для удаления заметки
        notesController.deleteNote(1L);

        // Проверяем, что метод noteService.deleteNoteById() был вызван с правильным аргументом
        verify(noteService, times(1)).deleteNoteById(1L);
    }
}
