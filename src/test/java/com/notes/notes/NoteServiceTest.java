package com.notes.notes;

import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @InjectMocks
    private NoteService noteService;

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private NoteMapper noteMapper;

    @Test
    void testGetAllNotes() {
        List<NoteEntity> noteEntities = new ArrayList<>();
        noteEntities.add(new NoteEntity(1L, "Заголовок 1", "Содержание 1"));
        noteEntities.add(new NoteEntity(2L, "Заголовок 2", "Содержание 2"));

        when(noteRepository.findAll()).thenReturn(noteEntities);

        List<NoteDto> noteDtos = new ArrayList<>();
        noteDtos.add(new NoteDto(1L, "Заголовок 1", "Содержание 1"));
        noteDtos.add(new NoteDto(2L, "Заголовок 2", "Содержание 2"));

        when(noteMapper.toDto(any())).thenReturn(noteDtos.get(0), noteDtos.get(1));

        List<NoteDto> result = noteService.getAllNotes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Заголовок 1", result.get(0).getTitle());
        assertEquals("Содержание 1", result.get(0).getContent());
        assertEquals("Заголовок 2", result.get(1).getTitle());
        assertEquals("Содержание 2", result.get(1).getContent());

        verify(noteRepository, times(1)).findAll();
        verify(noteMapper, times(2)).toDto(any());
    }

    @Test
    void testGetNoteById() {
        Long id = 1L;
        NoteEntity noteEntity = new NoteEntity(id, "Заголовок", "Содержание");

        when(noteRepository.findById(id)).thenReturn(Optional.of(noteEntity));

        NoteDto noteDto = new NoteDto(id, "Заголовок", "Содержание");
        when(noteMapper.toDto(noteEntity)).thenReturn(noteDto);

        NoteDto result = noteService.getNoteById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Заголовок", result.getTitle());
        assertEquals("Содержание", result.getContent());

        verify(noteRepository, times(1)).findById(id);
        verify(noteMapper, times(1)).toDto(noteEntity);
    }

    @Test
    void testUpdateNote() {
        Long id = 1L;
        NoteDto updatedNoteDto = new NoteDto(id, "Обновленный заголовок", "Обновленное содержание");

        assertThrows(NotFoundException.class, () -> noteService.updateNote(id, updatedNoteDto));

        verify(noteRepository, times(1)).findById(id);

        verify(noteRepository, never()).save(any());
    }

    @Test
    void testUpdateNoteWhenNoteNotFound() {
        Long id = 1L;
        NoteDto updatedNoteDto = new NoteDto(id, "Обновленный заголовок", "Обновленное содержание");

        when(noteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> noteService.updateNote(id, updatedNoteDto));

        verify(noteRepository, times(1)).findById(id);
        verify(noteRepository, never()).save(any());
    }

    @Test
    void testDeleteNoteById() {
        Long id = 1L;

        noteService.deleteNoteById(id);

        verify(noteRepository, times(1)).deleteById(id);
    }
}