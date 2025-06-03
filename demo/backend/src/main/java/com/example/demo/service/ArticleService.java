package com.example.demo.service;

import com.example.demo.dto.article.ArticleCreateUpdateDTO;
import com.example.demo.dto.article.ArticleDetailDTO;
import com.example.demo.dto.article.ArticleSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    // --- User Facing ---
    Page<ArticleSummaryDTO> getPublishedArticles(Pageable pageable);

    ArticleDetailDTO getPublishedArticleBySlug(String slug);

    // --- Admin Facing ---
    Page<ArticleSummaryDTO> getAdminArticles(String keyword, Boolean isPublished, Pageable pageable);


    ArticleDetailDTO getArticleByIdAdmin(Long id);


    ArticleDetailDTO createArticle(ArticleCreateUpdateDTO dto, String authorUsername);


    ArticleDetailDTO updateArticle(Long id, ArticleCreateUpdateDTO dto);


    void deleteArticle(Long id);

    ArticleDetailDTO publishArticle(Long id);


    ArticleDetailDTO unpublishArticle(Long id);
}