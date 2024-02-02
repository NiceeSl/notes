package com.notes.notes;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final NoteEventPublisher publisher;


    @Autowired
    public NoteService(NoteRepository noteRepository, NoteMapper noteMapper, NoteEventPublisher publisher) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
        this.publisher = publisher;
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

    public NoteDto createNote(NoteDto noteDto) throws NotFoundException {
        NoteEntity noteEntity = noteRepository.save(noteMapper.toEntity(noteDto));
        publisher.send(new NoteEventDto(EventType.CREATED, noteEntity.getId()));
        return noteMapper.toDto(noteEntity);
    }

    public NoteDto updateNote(Long id, NoteDto noteDto) throws NotFoundException {
        NoteEntity existingNoteEntity = noteRepository.findById(id).orElse(null);
        if (existingNoteEntity != null) {
            existingNoteEntity.setTitle(noteDto.getTitle());
            existingNoteEntity.setContent(noteDto.getContent());

            NoteEntity updatedNoteEntity = noteRepository.save(existingNoteEntity);
            return noteMapper.toDto(updatedNoteEntity);
        } else {
            throw new NotFoundException("Note with id " + id + " not found");
        }
    }

    public void deleteNoteById(Long id) {
        noteRepository.deleteById(id);
    }
}