package com.dean.baby.common.dto;

 import com.dean.baby.common.dto.common.LangFieldObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlashcardDTO {
    private UUID id;
    private UUID categoryId;               // 改為 CategoryDTO 對象
    private MilestoneBaseDTO milestone;             // 改為 MilestoneDTO 對象
    private int ageInMonths;                    // 關聯的 Milestone 的月齡
    private LangFieldObject subject;            // 主題/正面文字 (原 frontText)
    private String subjectString;                // 主題/正面文字 (從 LangFieldObject 取出目前語系字串)
    private String imageUrl;                    // 圖片URL (從 FlashcardTranslation 移過來)
    private List<FlashcardTranslationDTO> translations;

    public static FlashcardDTO fromEntity(com.dean.baby.common.entity.Flashcard flashcard) {
        if (flashcard == null) {
            return null;
        }
        return FlashcardDTO.builder()
                .id(flashcard.getId())
                .categoryId(flashcard.getCategory().getId())
                .milestone(MilestoneBaseDTO.fromEntity(flashcard.getMilestone()))
                .ageInMonths(flashcard.getMilestone() != null ? flashcard.getMilestone().getAgeInMonths() : 0)
                .subjectString(flashcard.getSubject() != null ? flashcard.getSubject().getLangByLocaleName() : "")
                .subject(flashcard.getSubject())
                .imageUrl(flashcard.getImageUrl())
                .translations(flashcard.getTranslations() != null ?
                    flashcard.getTranslations().stream()
                        .map(FlashcardTranslationDTO::fromEntity)
                        .toList() : null)
                .build();
    }
}
