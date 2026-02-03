package com.dean.baby.common.repository;

import com.dean.baby.common.entity.ArticleTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleTranslationRepository extends JpaRepository<ArticleTranslation, UUID> {
    List<ArticleTranslation> findByArticleIdAndLanguageCode(UUID articleId, String languageCode);
}
