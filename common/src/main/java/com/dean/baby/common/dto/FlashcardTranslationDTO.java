package com.dean.baby.common.dto;

import com.dean.baby.common.dto.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlashcardTranslationDTO {
    private UUID id;
    private Language languageCode; // 語言代碼，例如 "en" 或 "zh"
    private String frontText;   // 正面文字
    private String backText;    // 背面文字
    private String imageUrl;    // 圖片 URL

    public static FlashcardTranslationDTO fromEntity(com.dean.baby.common.entity.FlashcardTranslation translation) {
        if (translation == null) {
            return null;
        }
        return FlashcardTranslationDTO.builder()
                .id(translation.getId())
                .languageCode(translation.getLanguageCode() != null ? translation.getLanguageCode() : null)
                .frontText(translation.getFrontText())
                .backText(translation.getBackText())
                .imageUrl(translation.getImageUrl())
                .build();
    }
}
