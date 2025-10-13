package com.dean.baby.common.dto;

import com.dean.baby.common.dto.common.LangFieldObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class MilestoneBaseDTO {
    private UUID id;
    private String description;
    @JsonIgnore
    private LangFieldObject descriptionObject;

    public static MilestoneBaseDTO fromEntity(com.dean.baby.common.entity.Milestone milestone) {
        if (milestone == null) {
            return null;
        }
        return MilestoneBaseDTO.builder()
                .id(milestone.getId())
                .description(milestone.getDescription() != null ? milestone.getDescription().getLangByLocaleName() : "")
                .descriptionObject(milestone.getDescription())
                .build();
    }
}
