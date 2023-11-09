package com.notes.notes;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class NotesControllerTest extends AbstractSpringBootTest {

    @MockBean
    private NoteRepository noteRepository;

    @BeforeEach
    public void setup() {
        NoteEntity noteEntity = new NoteEntity(1L, "Sample Title 1", "Sample Content 1");
        NoteEntity noteEntity2 = new NoteEntity(2L, "Sample Title 2", "Sample Content 2");

        when(noteRepository.findById(1L)).thenReturn(Optional.of(noteEntity));
        when(noteRepository.findById(2L)).thenReturn(Optional.of(noteEntity2));
        when(noteRepository.findAll()).thenReturn(List.of(noteEntity, noteEntity2));
    }



    @Test
    public void testGetAllNotes() throws Exception {
        ResponseEntity<String> response = testRestTemplate.getForEntity("/notes", String.class);

        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());

        List<NoteDto> notes = objectMapper.readValue(
                response.getBody(),
                new TypeReference<>() {}
        );

        assertEquals(2, notes.size());

        NoteDto firstNote = notes.get(0);
        assertEquals(1L, firstNote.getId());
        assertEquals("Sample Title 1", firstNote.getTitle());
        assertEquals("Sample Content 1", firstNote.getContent());

        NoteDto secondNote = notes.get(1);
        assertEquals(2L, secondNote.getId());
        assertEquals("Sample Title 2", secondNote.getTitle());
        assertEquals("Sample Content 2", secondNote.getContent());
    }


    @Test
    public void testGetNoteById_WhenNoteExists() throws Exception {
        long noteId = 1L;

        ResponseEntity<String> response = testRestTemplate.getForEntity("/notes/" + noteId, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        NoteDto note = objectMapper.readValue(response.getBody(), NoteDto.class);

        assertNotNull(note);
        assertEquals(noteId, note.getId());
        assertNotNull(note.getTitle());
        assertNotNull(note.getContent());
    }


    @Test
    public void testGetNoteById_WhenNoteDoesNotExist() {
        long nonExistentNoteId = 10L;
        ResponseEntity<NoteDto> response = testRestTemplate.getForEntity("/notes/" + nonExistentNoteId, NoteDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateNote() {
        NoteDto newNote = new NoteDto();
        ResponseEntity<NoteDto> response = testRestTemplate.postForEntity("/notes", newNote, NoteDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void testUpdateNote_WhenNoteExists() {
        Long noteId = 1L;
        NoteDto updatedNote = new NoteDto();
        updatedNote.setTitle("Updated Title");
        updatedNote.setContent("Updated Content");

        NoteEntity existingNote = new NoteEntity(noteId, "Sample Title 1", "Sample Content 1");
        when(noteRepository.findById(noteId)).thenReturn(Optional.of(existingNote));
        when(noteRepository.save(any(NoteEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<NoteDto> response = testRestTemplate.exchange(
                "/notes/" + noteId,
                HttpMethod.PUT,
                new HttpEntity<>(updatedNote),
                NoteDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Title", response.getBody().getTitle());
        assertEquals("Updated Content", response.getBody().getContent());
    }


    @Test
    public void testDeleteNote() {
        long noteId = 1L;
        testRestTemplate.delete("/notes/" + noteId);
    }
}