package com.example.demo.controller;

import com.example.demo.dto.article.ArticleCreateUpdateDTO;
import com.example.demo.dto.article.ArticleDetailDTO;
import com.example.demo.dto.article.ArticleSummaryDTO;
import com.example.demo.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/articles")
@RequiredArgsConstructor
public class AdminArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<Page<ArticleSummaryDTO>> getAdminArticles(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean isPublished,
            @PageableDefault(size = 15, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ArticleSummaryDTO> articlePage = articleService.getAdminArticles(keyword, isPublished, pageable);
        return ResponseEntity.ok(articlePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDetailDTO> getArticleById(@PathVariable Long id) {
        ArticleDetailDTO article = articleService.getArticleByIdAdmin(id);
        return ResponseEntity.ok(article);
    }

    @PostMapping
    public ResponseEntity<ArticleDetailDTO> createArticle(
            @Valid @RequestBody ArticleCreateUpdateDTO dto) {

        ArticleDetailDTO createdArticle = articleService.createArticle(dto, "admin");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdArticle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDetailDTO> updateArticle(
            @PathVariable Long id,
            @Valid @RequestBody ArticleCreateUpdateDTO dto) {
        ArticleDetailDTO updatedArticle = articleService.updateArticle(id, dto);
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<ArticleDetailDTO> publishArticle(@PathVariable Long id) {
        ArticleDetailDTO publishedArticle = articleService.publishArticle(id);
        return ResponseEntity.ok(publishedArticle);
    }

    @PatchMapping("/{id}/unpublish")
    public ResponseEntity<ArticleDetailDTO> unpublishArticle(@PathVariable Long id) {
        ArticleDetailDTO unpublishedArticle = articleService.unpublishArticle(id);
        return ResponseEntity.ok(unpublishedArticle);
    }
}