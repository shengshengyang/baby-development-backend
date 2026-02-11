package com.dean.baby.dto;

import com.dean.baby.dto.common.LangFieldObject;
import com.dean.baby.entity.Milestone;
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
public class MilestoneBaseDTO {
    private UUID id;
    private String description;
    @JsonIgnore
    private LangFieldObject descriptionObject;

    public static MilestoneBaseDTO fromEntity(Milestone milestone) {
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
