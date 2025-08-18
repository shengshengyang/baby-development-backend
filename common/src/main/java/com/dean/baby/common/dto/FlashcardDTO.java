package com.dean.baby.common.dto;

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
    private UUID categoryId;                    // 分類
    private UUID milestoneId;                   // 關聯的 Milestone ID
    private int ageInMonths;                    // 關聯的 Milestone 的月齡
    private List<FlashcardTranslationDTO> translations;
}
