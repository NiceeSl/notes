package com.notes.notes;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    NoteEntity toEntity(NoteDto dto);
    NoteDto toDto(NoteEntity entity);
}

