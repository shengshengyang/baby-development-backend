package com.dean.baby.common.dto;

import com.dean.baby.common.dto.common.LangFieldObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    private UUID id;
    private String name;
    @JsonIgnore
    private LangFieldObject nameObject; // 完整的多語言物件

    /**
     * 從Category實體轉換為DTO，會根據當前語言環境返回對應的名稱
     * @param category Category實體
     * @return CategoryDTO
     */
    public static CategoryDTO fromEntity(com.dean.baby.common.entity.Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName() != null ? category.getName().getLangByLocaleName() : "")
                .nameObject(category.getName())
                .build();
    }

    /**
     * 從Category實體轉換為DTO，指定特定語言
     * @param category Category實體
     * @param languageCode 語言代碼 (如: tw, cn, en等)
     * @return CategoryDTO
     */
    public static CategoryDTO fromEntity(com.dean.baby.common.entity.Category category, String languageCode) {
        if (category == null) {
            return null;
        }
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName() != null ? category.getName().getLang(languageCode) : "")
                .nameObject(category.getName())
                .build();
    }
}
