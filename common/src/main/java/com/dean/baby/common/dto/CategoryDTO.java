package com.dean.baby.common.dto;

import com.dean.baby.common.dto.enums.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    private UUID id;
    private String name;

    public static CategoryDTO fromEntity(com.dean.baby.common.entity.Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName().get(Language.TRADITIONAL_CHINESE))
                .build();
    }
}
