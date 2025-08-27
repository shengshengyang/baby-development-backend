package com.dean.baby.common.dto;

import com.dean.baby.common.entity.Age;
import lombok.Data;

import java.util.UUID;

@Data
public class AgeDto {
    private UUID id;
    private int month;
    private String displayName;
    public static AgeDto fromEntity(Age age) {
        if (age == null) {
            return null;
        }
        AgeDto dto = new AgeDto();
        dto.setId(age.getId());
        dto.setMonth(age.getMonth());
        dto.setDisplayName(age.getDisplayName() != null ? age.getDisplayName().getLangByLocaleName() : "");
        return dto;
    }
}
