package com.dean.baby.common.dto;

import com.dean.baby.common.entity.Article;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ArticleDto {

    private UUID id;

    private CategoryDTO category;

    private List<ArticleTranslationDto> translations;

    public static ArticleDto fromEntity(Article article) {
        if (article == null) {
            return null;
        }
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(article.getId());
        articleDto.setCategory(CategoryDTO.fromEntity(article.getCategory()));
        if (article.getTranslations() != null) {
            List<ArticleTranslationDto> translationDtos = article.getTranslations().stream().map(ArticleTranslationDto::fromEntity).toList();
            articleDto.setTranslations(translationDtos);
        }
        return articleDto;
    }
}
