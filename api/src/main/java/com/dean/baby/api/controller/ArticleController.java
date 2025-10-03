package com.dean.baby.api.controller;

import com.dean.baby.common.dto.ArticleResponseDto;
import com.dean.baby.common.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * Get article by ID with localized content based on Accept-Language header
     * Default language: Traditional Chinese (tw)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponseDto> getArticleById(@PathVariable UUID id) {
        Optional<ArticleResponseDto> article = articleService.getArticleById(id);
        return article.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all articles or search by category with localized content
     * Language is determined by Accept-Language header, defaults to Traditional Chinese (tw)
     * @param categoryId optional category ID to filter by
     */
    @GetMapping
    public ResponseEntity<List<ArticleResponseDto>> getArticles(
            @RequestParam(required = false) UUID categoryId) {
        List<ArticleResponseDto> articles = articleService.getArticles(categoryId);
        return ResponseEntity.ok(articles);
    }

}
