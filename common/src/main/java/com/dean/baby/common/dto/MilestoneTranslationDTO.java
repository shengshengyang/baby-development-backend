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
public class MilestoneTranslationDTO {
    private UUID id;
    private String languageCode;
    private String description;

    public static MilestoneTranslationDTO fromEntity(com.dean.baby.common.entity.MilestoneTranslation translation) {
        if (translation == null) {
            return null;
        }
        return MilestoneTranslationDTO.builder()
                .id(translation.getId())
                .languageCode(translation.getLanguageCode())
                .description(translation.getDescription())
                .build();
    }
}
