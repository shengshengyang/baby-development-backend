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
    private UUID categoryId;                    // 分類
    private UUID milestoneId;                   // 關聯的 月齡
    private int ageInMonths;                    // 關聯的 Milestone 的月齡
    private String languageCode; // 語言代碼，例如 "en" 或 "zh"
    private String frontText;   // 正面文字
    private String backText;    // 背面文字
    private String imageUrl;    // 圖片 URL
}
