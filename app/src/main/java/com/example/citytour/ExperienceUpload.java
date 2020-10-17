package com.example.citytour;

public class ExperienceUpload {
    private String title;
    private String note;
    private String key;

    public ExperienceUpload() {

    }

    public ExperienceUpload(String title, String note, String key)  {
        this.title = title;
        this.note = note;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
