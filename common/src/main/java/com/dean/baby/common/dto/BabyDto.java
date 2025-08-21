package com.dean.baby.common.dto;

import com.dean.baby.common.entity.Baby;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class BabyDto {
    private UUID id;
    private String name;
    private LocalDate birthDate;
    private List<ProgressDto> progresses;

    public static BabyDto fromEntity(Baby baby) {
        return BabyDto.builder()
                .id(baby.getId())
                .name(baby.getName())
                .birthDate(baby.getBirthDate())
                .progresses(baby.getProgresses() == null ? null : ProgressDto.fromEntities(baby.getProgresses()))
                .build();
    }
}
