package com.notes.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {
    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<NoteDto> getAllNotes() {
        List<NoteEntity> noteEntities = noteRepository.findAll();
        return noteEntities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public NoteDto getNoteById(Long id) {
        NoteEntity noteEntity = noteRepository.findById(id).orElse(null);
        if (noteEntity != null) {
            return convertToDto(noteEntity);
        } else {
            return null;
        }
    }

    public NoteDto createNote(NoteDto noteDto) {
        NoteEntity noteEntity = convertToEntity(noteDto);
        NoteEntity createdNoteEntity = noteRepository.save(noteEntity);
        return convertToDto(createdNoteEntity);
    }

    public NoteDto updateNote(Long id, NoteDto noteDto) {
        NoteEntity existingNoteEntity = noteRepository.findById(id).orElse(null);
        if (existingNoteEntity != null) {
            existingNoteEntity.setTitle(noteDto.getTitle());
            existingNoteEntity.setContent(noteDto.getContent());

            NoteEntity updatedNoteEntity = noteRepository.save(existingNoteEntity);
            return convertToDto(updatedNoteEntity);
        } else {
            return null;
        }
    }

    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }

    public NoteDto convertToDto(NoteEntity noteEntity) {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(noteEntity.getId());
        noteDto.setTitle(noteEntity.getTitle());
        noteDto.setContent(noteEntity.getContent());
        return noteDto;
    }

    public NoteEntity convertToEntity(NoteDto noteDto) {
        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setTitle(noteDto.getTitle());
        noteEntity.setContent(noteDto.getContent());
        return noteEntity;
    }
}
