package com.dean.baby.repository;

import com.dean.baby.entity.ArticleTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleTranslationRepository extends JpaRepository<ArticleTranslation, Long> {
    List<ArticleTranslation> findByArticleIdAndLanguageCode(Long articleId, String languageCode);
}
