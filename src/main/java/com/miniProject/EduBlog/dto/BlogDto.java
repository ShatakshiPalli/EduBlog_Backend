package com.miniProject.EduBlog.dto;

import java.time.LocalDateTime;

public class BlogDto {
    private String id;
    private String title;
    private String category;
    private String description;
    private String content;
    private LocalDateTime createdAt;
    private UserDto author;

    // ✅ Default constructor (needed for frameworks like Spring)
    public BlogDto() {
    }

    // ✅ Parameterized constructor
    public BlogDto(String id, String title, String category, String description) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructor with all fields
    public BlogDto(String id, String title, String category, String description, String content, LocalDateTime createdAt, UserDto author) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.content = content;
        this.createdAt = createdAt;
        this.author = author;
    }

    // ✅ Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public UserDto getAuthor() {
        return author;
    }

    public void setAuthor(UserDto author) {
        this.author = author;
    }
}
