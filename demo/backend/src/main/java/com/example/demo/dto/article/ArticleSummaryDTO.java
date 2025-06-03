package com.example.demo.dto.article;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ArticleSummaryDTO {
    private Long id;
    private String title;
    private String slug;
    private String excerpt;
    private String featuredImageUrl;
    private String authorUsername;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private boolean IsPublished;
}