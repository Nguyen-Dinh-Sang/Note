package com.example.mynote.model;

import java.io.Serializable;

public class NoteItem implements Serializable {
    private long timeAndId;
    private String title;
    private String content;

    public long getTimeAndId() {
        return timeAndId;
    }

    public void setTimeAndId(long timeAndId) {
        this.timeAndId = timeAndId;
    }

    public String getTitle() {
        return title;
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

    public NoteItem(long timeAndId, String title, String content) {
        this.timeAndId = timeAndId;
        this.title = title;
        this.content = content;
    }
}
