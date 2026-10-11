package com.farmconnect.dto;

import com.farmconnect.entity.Season;

import java.time.LocalDateTime;

public class FarmingTipResponse {
    private Long id;
    private String title;
    private String category;
    private Season season;
    private String content;
    private String author;
    private LocalDateTime publishedAt;

    public FarmingTipResponse() {
    }

    public FarmingTipResponse(Long id, String title, String category, Season season,
                              String content, String author, LocalDateTime publishedAt) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.season = season;
        this.content = content;
        this.author = author;
        this.publishedAt = publishedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }
}
