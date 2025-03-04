package com.dean.baby.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardDTO {
    private Long id;
    private String category;                    // 分類
    private Long milestoneId;                   // 關聯的 Milestone ID
    private List<FlashcardTranslationDTO> translations;
}
