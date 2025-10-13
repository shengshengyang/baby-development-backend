package com.dean.baby.common.service;

import com.dean.baby.common.dto.ArticleCreateRequestVo;
import com.dean.baby.common.dto.ArticleDto;
import com.dean.baby.common.dto.ArticleResponseDto;
import com.dean.baby.common.dto.ArticleTranslationRequestVo;
import com.dean.baby.common.dto.enums.Language;
import com.dean.baby.common.entity.Article;
import com.dean.baby.common.entity.ArticleTranslation;
import com.dean.baby.common.entity.Category;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.ArticleRepository;
import com.dean.baby.common.repository.CategoryRepository;
import com.dean.baby.common.repository.UserRepository;
import com.dean.baby.common.util.LanguageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
     * List articles with optional category filter and language filter
     * @param categoryId optional category ID to filter by
     * @param languageCode optional language code to filter translations
     * @return list of articles with translations
     */
    public List<ArticleDto> listArticles(UUID categoryId, String languageCode) {
        if (categoryId != null) {
            Category category = findCategoryById(categoryId);
            return articleRepository.findAll().stream()
                    .filter(article -> article.getCategory().getId().equals(categoryId))
                    .map(article -> ArticleDto.fromEntity(article, languageCode))
                    .toList();
        }
        return articleRepository.findAll().stream()
                .map(article -> ArticleDto.fromEntity(article, languageCode))
                .toList();
    }

    /**
     * Get article by ID with optional language filter
     * @param id article ID
     * @param languageCode optional language code to filter translations
     * @return ArticleDto with translations
     */
    public ArticleDto getArticle(UUID id, String languageCode) {
        return articleRepository.findById(id)
                .map(article -> ArticleDto.fromEntity(article, languageCode))
                .orElseThrow(() -> new ApiException(SysCode.ARTICLE_NOT_FOUND));
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

    /**
     * Create or update an article with translations
     * @param vo ArticleCreateRequestVo containing article data
     * @return ArticleResponseDto with the created/updated article
     */
    @Transactional
    public ArticleResponseDto createOrUpdateArticle(ArticleCreateRequestVo vo) {
        Article article;

        if (vo.id() != null) {
            // Update existing article
            article = articleRepository.findById(vo.id())
                    .orElseThrow(() -> new ApiException(SysCode.ARTICLE_NOT_FOUND));
            // Clear existing translations
            article.getTranslations().clear();
        } else {
            // Create new article
            article = new Article();
        }

        // Set category
        article.setCategory(findCategoryById(vo.categoryId()));

        // Set translations
        List<ArticleTranslation> translations = vo.translations().stream()
                .map(translationVo -> createArticleTranslation(article, translationVo))
                .collect(Collectors.toList());
        article.setTranslations(translations);

        // Save article
        Article savedArticle = articleRepository.save(article);

        // Return response with current locale
        Language language = LanguageUtil.getLanguageFromLocale();
        return ArticleResponseDto.fromEntity(savedArticle, language);
    }

    /**
     * Delete an article by ID
     * @param id article ID to delete
     */
    @Transactional
    public void deleteArticle(UUID id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ApiException(SysCode.ARTICLE_NOT_FOUND));
        articleRepository.delete(article);
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

    private ArticleTranslation createArticleTranslation(Article article, ArticleTranslationRequestVo vo) {
        ArticleTranslation translation = new ArticleTranslation();
        translation.setArticle(article);
        translation.setLanguageCode(vo.languageCode());
        translation.setTitle(vo.title());
        translation.setContent(vo.content());
        return translation;
    }
}
