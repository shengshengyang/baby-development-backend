package com.dean.baby.common.service;

import com.dean.baby.common.dto.ArticleDto;
import com.dean.baby.common.dto.ArticleResponseDto;
import com.dean.baby.common.dto.enums.Language;
import com.dean.baby.common.entity.Category;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.ArticleRepository;
import com.dean.baby.common.repository.CategoryRepository;
import com.dean.baby.common.repository.UserRepository;
import com.dean.baby.common.util.LanguageUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ArticleService extends BaseService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    public ArticleService(UserRepository userRepository,
                         ArticleRepository articleRepository,
                         CategoryRepository categoryRepository) {
        super(userRepository);
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Get article by ID with localized content
     * @param id article ID
     * @return ArticleResponseDto with translation for current locale
     */
    public Optional<ArticleResponseDto> getArticleById(UUID id) {
        Language language = LanguageUtil.getLanguageFromLocale();
        return articleRepository.findById(id)
                .map(article -> ArticleResponseDto.fromEntity(article, language));
    }

    /**
     * Get articles with optional category filter and localized content
     * @param categoryId optional category ID to filter by
     * @return list of articles with translations for current locale
     */
    public List<ArticleResponseDto> getArticles(UUID categoryId) {
        Language language = LanguageUtil.getLanguageFromLocale();
        
        if (categoryId != null) {
            return getArticlesByCategory(categoryId, language);
        }
        return getAllArticles(language);
    }

    private List<ArticleResponseDto> getAllArticles(Language language) {
        return articleRepository.findAll().stream()
                .map(article -> ArticleResponseDto.fromEntity(article, language))
                .toList();
    }

    private List<ArticleResponseDto> getArticlesByCategory(UUID categoryId, Language language) {
        Category category = findCategoryById(categoryId);
        return articleRepository.findAll().stream()
                .filter(article -> article.getCategory().getId().equals(categoryId))
                .map(article -> ArticleResponseDto.fromEntity(article, language))
                .toList();
    }

    // === Private Helper Methods ===

    private Category findCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(SysCode.CATEGORY_NOT_FOUND));
    }
}
