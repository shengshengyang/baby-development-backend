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
    private AgeDto age;
    private CategoryDTO category;

    // 新增：多語描述（���供目前語系字串與完整物件）
    private String description;
    private LangFieldObject descriptionObject;

    // 新增：影片連結
    private String videoUrl;

    // 新增：base64 圖片
    private String imageBase64;

    public static MilestoneDTO fromEntity(com.dean.baby.common.entity.Milestone milestone) {
        if (milestone == null) {
            return null;
        }
        return MilestoneDTO.builder()
                .id(milestone.getId())
                .age(AgeDto.fromEntity(milestone.getAge()))
                .category(CategoryDTO.fromEntity(milestone.getCategory()))
                .description(milestone.getDescription() != null ? milestone.getDescription().getLangByLocaleName() : "")
                .descriptionObject(milestone.getDescription())
                .videoUrl(milestone.getVideoUrl())
                .imageBase64(milestone.getImageBase64())
                .build();
    }
}
