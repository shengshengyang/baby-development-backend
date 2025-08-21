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
public class MilestoneDTO {
    private UUID id;
    private int ageInMonths;
    private CategoryDTO category;
    private List<MilestoneTranslationDTO> translations;

    public static MilestoneDTO fromEntity(com.dean.baby.common.entity.Milestone milestone) {
        if (milestone == null) {
            return null;
        }
        return MilestoneDTO.builder()
                .id(milestone.getId())
                .ageInMonths(milestone.getAgeInMonths())
                .category(CategoryDTO.fromEntity(milestone.getCategory()))
                .translations(milestone.getTranslations() != null ?
                    milestone.getTranslations().stream()
                        .map(MilestoneTranslationDTO::fromEntity)
                        .toList() : null)
                .build();
    }
}
