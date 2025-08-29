package com.dean.baby.common.dto;

import com.dean.baby.common.entity.Article;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ArticalDto {

    private UUID id;

    private CategoryDTO category;

    private List<ArticleTranslationDto> translations;

    public static ArticalDto fromEntity(Article article) {
        if (article == null) {
            return null;
        }
        ArticalDto articalDto = new ArticalDto();
        articalDto.setId(article.getId());
        articalDto.setCategory(CategoryDTO.fromEntity(article.getCategory()));
        if (article.getTranslations() != null) {
            List<ArticleTranslationDto> translationDtos = article.getTranslations().stream().map(ArticleTranslationDto::fromEntity).toList();
            articalDto.setTranslations(translationDtos);
        }
        return articalDto;
    }
}
