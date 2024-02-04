package com.notes.notes;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
        NoteEventPayloadDto payload = new NoteEventPayloadDto(noteDto.getTitle(), noteDto.getContent());
        publisher.send(new NoteEventDto(EventType.CREATED, noteEntity.getId()), payload);
        return noteMapper.toDto(noteEntity);
    }

    public NoteDto updateNote(Long id, NoteDto noteDto) throws NotFoundException {
        NoteEntity noteEntity = noteRepository.findById(id).orElse(null);
        NoteEventPayloadDto payload = new NoteEventPayloadDto(noteDto.getTitle(), noteDto.getContent());
        if (noteEntity != null) {
            noteEntity.setTitle(noteDto.getTitle());
            noteEntity.setContent(noteDto.getContent());

            NoteEntity updatedNoteEntity = noteRepository.save(noteEntity);
            publisher.send(new NoteEventDto(EventType.UPDATED, noteEntity.getId()), payload);
            return noteMapper.toDto(updatedNoteEntity);
        } else {
            throw new NotFoundException("Note with id " + id + " not found");
        }
    }

    public void deleteNoteById(Long id) throws NotFoundException{
        NoteEntity noteEntity = noteRepository.findById(id).orElse(null);
        if(noteEntity != null) {
            publisher.send(new NoteEventDto(EventType.DELETED, noteEntity.getId()), null);
            noteRepository.deleteById(id);
        }
        else {
            throw new NotFoundException("Note with id " + id + " not found");
        }
    }
}