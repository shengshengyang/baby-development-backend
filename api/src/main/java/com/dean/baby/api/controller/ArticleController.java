package com.dean.baby.api.controller;

import com.dean.baby.common.dto.ArticleDto;
import com.dean.baby.common.service.ArticleService;
import com.dean.baby.common.util.LanguageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/article")
@RestController
@Tag(name = "文章管理", description = "寶寶發展相關文章管理 API")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Operation(summary = "獲取文章列表", description = "根據分類 ID 獲取文章列表，未指定分類時返回所有文章")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "獲取成功")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ArticleDto>> listArticles(
            @Parameter(description = "分類 ID（可選）") @RequestParam(required = false) UUID categoryId) {
        String languageCode = LanguageUtil.getLanguageCode(LanguageUtil.getLanguageFromLocale());
        return ResponseEntity.ok(articleService.listArticles(categoryId, languageCode));
    }

    @Operation(summary = "獲取文章詳情", description = "根據 ID 獲取單個文章的詳細信息")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "獲取成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "文章不存在")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ArticleDto> getArticle(
            @Parameter(description = "文章 ID") @PathVariable UUID id) {
        String languageCode = LanguageUtil.getLanguageCode(LanguageUtil.getLanguageFromLocale());
        return ResponseEntity.ok(articleService.getArticle(id, languageCode));
    }
}
