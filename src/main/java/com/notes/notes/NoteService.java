package com.notes.notes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    @Autowired
    public NoteService(NoteRepository noteRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
    }

    public List<NoteDto> getAllNotes() {
        List<NoteEntity> noteEntities = noteRepository.findAll();
        return noteEntities.stream()
                .map(noteMapper::toDto)
                .collect(Collectors.toList());
    }

    public NoteDto getNoteById(Long id) {
        NoteEntity noteEntity = noteRepository.findById(id).orElse(null);
        return (noteEntity != null) ? noteMapper.toDto(noteEntity) : null;
    }

    public NoteDto createNote(NoteDto noteDto) {
        NoteEntity noteEntity = noteMapper.toEntity(noteDto);
        NoteEntity createdNoteEntity = noteRepository.save(noteEntity);
        return noteMapper.toDto(createdNoteEntity);
    }

    public NoteDto updateNote(Long id, NoteDto noteDto) {
        NoteEntity existingNoteEntity = noteRepository.findById(id).orElse(null);
        if (existingNoteEntity != null) {
            existingNoteEntity.setTitle(noteDto.getTitle());
            existingNoteEntity.setContent(noteDto.getContent());

            NoteEntity updatedNoteEntity = noteRepository.save(existingNoteEntity);
            return noteMapper.toDto(updatedNoteEntity);
        } else {
            return null;
        }
    }

    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }
}
