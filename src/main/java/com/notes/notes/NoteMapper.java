package com.notes.notes;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface NoteMapper {
    NoteMapper INSTANCE = Mappers.getMapper(NoteMapper.class);

    @Mapping(target = "id", ignore = true)
    NoteEntity dtoToEntity(NoteDto dto);

    @Mapping(target = "id", ignore = true)
    NoteDto entityToDto(NoteEntity entity);
}

