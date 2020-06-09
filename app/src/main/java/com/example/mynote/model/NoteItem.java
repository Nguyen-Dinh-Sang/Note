package com.example.mynote.model;

import java.io.Serializable;
import java.lang.reflect.Type;

public class NoteItem implements Serializable {
    private long timeAndId;
    private String title;
    private String content;
    private float contentTextSize;
    private int contentTextColor;
    public enum TYPE {NormalNote,DrawNote};
    private TYPE type =TYPE.NormalNote;


    public float getContentTextSize() {
        return contentTextSize;
    }

    public void setContentTextSize(float contentTextSize) {
        this.contentTextSize = contentTextSize;
    }

    public int getContentTextColor() {
        return contentTextColor;
    }

    public void setContentTextColor(int contentTextColor) {
        this.contentTextColor = contentTextColor;
    }

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

    public NoteItem(long timeAndId, String title, String content,int contentTextColor,float contentTextSize,TYPE type) {
        this.timeAndId = timeAndId;
        this.title = title;
        this.content = content;
        this.type = type;
        this.contentTextColor=contentTextColor;
        this.contentTextSize=contentTextSize;
    }

}
