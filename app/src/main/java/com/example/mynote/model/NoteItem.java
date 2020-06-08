package com.example.mynote.model;

import java.io.Serializable;
import java.lang.reflect.Type;

public class NoteItem implements Serializable {
    private long timeAndId;
    private String title;
    private String content;
    public enum TYPE {NormalNote,DrawNote};
    private TYPE type =TYPE.NormalNote;

    public long getTimeAndId() {
        return timeAndId;
    }

    public void setTimeAndId(long timeAndId) {
        this.timeAndId = timeAndId;
    }

    public String getTitle() {
        return title;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NoteItem(long timeAndId, String title, String content,TYPE type) {
        this.timeAndId = timeAndId;
        this.title = title;
        this.content = content;
        this.type = type;
    }
    public NoteItem(long timeAndId, String title, String content) {
        this.timeAndId = timeAndId;
        this.title = title;
        this.content = content;
    }
}
