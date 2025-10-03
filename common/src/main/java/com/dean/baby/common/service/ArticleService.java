package com.dean.baby.common.service;

import com.dean.baby.common.dto.ArticalDto;
import com.dean.baby.common.entity.Article;
import com.dean.baby.common.entity.Category;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.ArticleRepository;
import com.dean.baby.common.repository.CategoryRepository;
import com.dean.baby.common.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Optional<ArticalDto> getArticleById(UUID id) {
        return articleRepository.findById(id).map(ArticalDto::fromEntity);
    }

    /**
     * Get articles with optional category filter
     * @param categoryId optional category ID to filter by
     * @return list of articles
     */
    public List<ArticalDto> getArticles(UUID categoryId) {
        if (categoryId != null) {
            return getArticlesByCategory(categoryId);
        }
        return getAllArticles();
    }

    private List<ArticalDto> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(ArticalDto::fromEntity)
                .toList();
    }

    private List<ArticalDto> getArticlesByCategory(UUID categoryId) {
        Category category = findCategoryById(categoryId);
        return articleRepository.findAll().stream()
                .filter(article -> article.getCategory().getId().equals(categoryId))
                .map(ArticalDto::fromEntity)
                .toList();
    }

    // === Private Helper Methods ===

    private Category findCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(SysCode.CATEGORY_NOT_FOUND));
    }
}
