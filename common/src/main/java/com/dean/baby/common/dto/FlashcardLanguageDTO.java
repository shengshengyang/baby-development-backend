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
    private String subject;    // 主題/正面文字（從 Flashcard.subject 依當前語系）
    private String description;    // 描述文字（翻譯）
    private String imageUrl;    // 圖片 URL（從 Flashcard）

    public static FlashcardLanguageDTO fromEntityWithLanguage(com.dean.baby.common.entity.Flashcard flashcard,
                                                             com.dean.baby.common.entity.FlashcardTranslation translation) {
        if (flashcard == null) {
            return null;
        }
        String subjectText = flashcard.getSubject() != null ? flashcard.getSubject().getLangByLocaleName() : null;
        return FlashcardLanguageDTO.builder()
                .id(flashcard.getId())
                .category(CategoryDTO.fromEntity(flashcard.getCategory()))
                .milestone(MilestoneDTO.fromEntity(flashcard.getMilestone()))
                .languageCode(translation != null && translation.getLanguageCode() != null ? translation.getLanguageCode().getCode() : null)
                .subject(subjectText)
                .description(translation != null ? translation.getDescription() : null)
                .imageUrl(flashcard.getImageUrl())
                .build();
    }
}
