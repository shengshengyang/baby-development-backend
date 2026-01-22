package com.dean.baby.api.controller;

import com.dean.baby.common.dto.ArticleDto;
import com.dean.baby.common.service.ArticleService;
import com.dean.baby.common.util.LanguageUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/article")
@RestController
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<ArticleDto> listArticles(
            @RequestParam(required = false) UUID categoryId) {
        String languageCode = LanguageUtil.getLanguageCode(LanguageUtil.getLanguageFromLocale());
        return articleService.listArticles(categoryId, languageCode);
    }

    @GetMapping("/{id}")
    public ArticleDto getArticle(@PathVariable UUID id) {
        String languageCode = LanguageUtil.getLanguageCode(LanguageUtil.getLanguageFromLocale());
        return articleService.getArticle(id, languageCode);
    }
}
