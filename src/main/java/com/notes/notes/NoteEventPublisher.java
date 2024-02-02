package com.notes.notes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NoteEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    @Autowired
    public NoteEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }


    public void send(NoteEventDto eventDto) {
        try {
            String data = mapper.writeValueAsString(eventDto);
            kafkaTemplate.send("note-events", data);
            System.out.println(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

