package com.dean.baby.common.dto;

import com.dean.baby.common.dto.enums.Language;
import com.dean.baby.common.entity.ArticleTranslation;
import lombok.Data;

import java.util.UUID;

@Data
public class ArticleTranslationDto {

    private UUID id;

    private String articleId;

    private Language languageCode;

    private String title;

    private String content;

    public static ArticleTranslationDto fromEntity(ArticleTranslation translation) {
        if (translation == null) {
            return null;
        }
        ArticleTranslationDto dto = new ArticleTranslationDto();
        dto.setId(translation.getId());
        dto.setArticleId(translation.getArticle() != null ? translation.getArticle().getId().toString() : null);
        dto.setLanguageCode(translation.getLanguageCode());
        dto.setTitle(translation.getTitle());
        dto.setContent(translation.getContent());
        return dto;
    }
}
