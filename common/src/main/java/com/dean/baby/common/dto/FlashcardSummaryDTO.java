package com.dean.baby.common.dto;

import com.dean.baby.common.dto.enums.Language;
import com.dean.baby.common.util.LanguageUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlashcardSummaryDTO {
    private UUID id;
    private CategoryDTO category;
    private String subject;                     // 當前語系的主題文字
    private String imageUrl;                    // 圖片URL
    private String description;                 // 當前語系的描述文字

    public static FlashcardSummaryDTO fromEntity(com.dean.baby.common.entity.Flashcard flashcard) {
        if (flashcard == null) {
            return null;
        }

        // 取得當前語言
        Language currentLanguage = LanguageUtil.getLanguageFromLocale();

        // 取得當前語系的 subject
        String subject = flashcard.getSubject() != null
            ? flashcard.getSubject().getLang(currentLanguage.getCode())
            : "";

        // 取得當前語系的 description
        String description = "";
        if (flashcard.getTranslations() != null) {
            description = flashcard.getTranslations().stream()
                .filter(translation -> translation.getLanguageCode() == currentLanguage)
                .findFirst()
                .map(com.dean.baby.common.entity.FlashcardTranslation::getDescription)
                .orElse("");
        }

        return FlashcardSummaryDTO.builder()
                .id(flashcard.getId())
                .category(CategoryDTO.fromEntity(flashcard.getCategory()))
                .subject(subject != null ? subject : "")
                .imageUrl(flashcard.getImageUrl())
                .description(description)
                .build();
    }
}
