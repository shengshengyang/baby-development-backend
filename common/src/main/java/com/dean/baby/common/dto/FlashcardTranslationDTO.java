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
    private Language languageCode; // 語言代碼，例如 TRADITIONAL_CHINESE, ENGLISH 等
    private String description;    // 描述文字（原 backText）

    public static FlashcardTranslationDTO fromEntity(com.dean.baby.common.entity.FlashcardTranslation translation) {
        if (translation == null) {
            return null;
        }
        return FlashcardTranslationDTO.builder()
                .id(translation.getId())
                .languageCode(translation.getLanguageCode())
                .description(translation.getDescription())
                .build();
    }
}
