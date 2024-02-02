package com.notes.notes;

public class NoteEventDto {
    private EventType type;
    private Long id;

    public NoteEventDto(EventType type, Long id) {
        this.type = type;
        this.id = id;
    }

    public EventType getType() {
        return type;
    }

    public Long getId() {
        return id;
    }
}
