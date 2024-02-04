package com.notes.notes;

public class NoteEventPayloadDto {
    private String title;
    private String content;

    public NoteEventPayloadDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
