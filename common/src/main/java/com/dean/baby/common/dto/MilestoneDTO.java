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
public class MilestoneDTO {
    private UUID id;
    private int ageInMonths;
    private CategoryDTO category;

    // 新增：多語描述（提供目前語系字串與完整物件）
    private String description;
    @JsonIgnore
    private LangFieldObject descriptionObject;

    // 新增：影片連結
    private String videoUrl;

    public static MilestoneDTO fromEntity(com.dean.baby.common.entity.Milestone milestone) {
        if (milestone == null) {
            return null;
        }
        return MilestoneDTO.builder()
                .id(milestone.getId())
                .ageInMonths(milestone.getAgeInMonths())
                .category(CategoryDTO.fromEntity(milestone.getCategory()))
                .description(milestone.getDescription() != null ? milestone.getDescription().getLangByLocaleName() : "")
                .descriptionObject(milestone.getDescription())
                .videoUrl(milestone.getVideoUrl())
                .build();
    }
}
