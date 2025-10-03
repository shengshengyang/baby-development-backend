package com.dean.baby.common.dto;

import com.dean.baby.common.dto.enums.Language;
import com.dean.baby.common.entity.Article;
import com.dean.baby.common.entity.ArticleTranslation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Article response DTO with localized content
 * Returns only one translation based on the requested language
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponseDto {

    private UUID id;

    private CategoryDTO category;

    private String title;

    private String content;

    private Language language;

    /**
     * Create DTO from Article entity with specific language
     * @param article the article entity
     * @param language the requested language
     * @return ArticleResponseDto with localized content
     */
    public static ArticleResponseDto fromEntity(Article article, Language language) {
        if (article == null) {
            return null;
        }

        ArticleResponseDto dto = new ArticleResponseDto();
        dto.setId(article.getId());
        dto.setCategory(CategoryDTO.fromEntity(article.getCategory()));
        dto.setLanguage(language);

        // Find translation for the requested language
        if (article.getTranslations() != null) {
            ArticleTranslation translation = article.getTranslations().stream()
                    .filter(t -> t.getLanguageCode() == language)
                    .findFirst()
                    .orElse(null);

            if (translation != null) {
                dto.setTitle(translation.getTitle());
                dto.setContent(translation.getContent());
            } else {
                // Fallback to Traditional Chinese if requested language not found
                translation = article.getTranslations().stream()
                        .filter(t -> t.getLanguageCode() == Language.TRADITIONAL_CHINESE)
                        .findFirst()
                        .orElse(null);

                if (translation != null) {
                    dto.setTitle(translation.getTitle());
                    dto.setContent(translation.getContent());
                }
            }
        }

        return dto;
    }
}
