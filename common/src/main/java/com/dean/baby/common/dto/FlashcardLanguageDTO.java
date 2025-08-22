package com.dean.baby.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlashcardLanguageDTO {
    private UUID id;
    private CategoryDTO category;               // 改為 CategoryDTO 對象
    private MilestoneDTO milestone;             // 改為 MilestoneDTO 對象
    private String languageCode; // 語言代碼，例如 "en" 或 "zh"
    private String frontText;   // 正面文字
    private String backText;    // 背面文字
    private String imageUrl;    // 圖片 URL

    public static FlashcardLanguageDTO fromEntityWithLanguage(com.dean.baby.common.entity.Flashcard flashcard,
                                                             com.dean.baby.common.entity.FlashcardTranslation translation) {
        if (flashcard == null) {
            return null;
        }
        return FlashcardLanguageDTO.builder()
                .id(flashcard.getId())
                .category(CategoryDTO.fromEntity(flashcard.getCategory()))
                .milestone(MilestoneDTO.fromEntity(flashcard.getMilestone()))
                .languageCode(translation != null && translation.getLanguageCode() != null ? translation.getLanguageCode().getCode() : null)
                .frontText(translation != null ? translation.getFrontText() : null)
                .backText(translation != null ? translation.getBackText() : null)
                .imageUrl(translation != null ? translation.getImageUrl() : null)
                .build();
    }
}
