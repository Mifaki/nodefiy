package com.example.notedify_recyclerview;

public class Notes {
    private String title;
    private String content;
    private String category;
    private String date;

    public Notes(String title, String content, String category, String date) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.date = date;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}