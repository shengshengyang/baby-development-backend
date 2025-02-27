package com.dean.baby.common.repository;

import com.dean.baby.common.entity.ArticleTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleTranslationRepository extends JpaRepository<ArticleTranslation, Long> {
    List<ArticleTranslation> findByArticleIdAndLanguageCode(Long articleId, String languageCode);
}
