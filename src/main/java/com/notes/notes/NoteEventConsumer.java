package com.notes.notes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NoteEventConsumer {

    private final ObjectMapper objectMapper;

    public NoteEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "note-events", groupId = "notes-group")
    public void listen(String message) {
        try {
            NoteEventDto noteEventDto = objectMapper.readValue(message, NoteEventDto.class);
            System.out.println("Received note event: " + noteEventDto);
        } catch (Exception e) {
            System.err.println("Error converting JSON to NoteEventDto: " + e.getMessage());
        }
    }
}
