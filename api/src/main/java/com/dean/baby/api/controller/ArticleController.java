package com.dean.baby.api.controller;

import com.dean.baby.common.dto.ArticalDto;
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
     * Get article by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArticalDto> getArticleById(@PathVariable UUID id) {
        Optional<ArticalDto> article = articleService.getArticleById(id);
        return article.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Get all articles or search by category
     * @param categoryId optional category ID to filter by
     */
    @GetMapping
    public ResponseEntity<List<ArticalDto>> getArticles(
            @RequestParam(required = false) UUID categoryId) {
        List<ArticalDto> articles = articleService.getArticles(categoryId);
        return ResponseEntity.ok(articles);
    }

}
