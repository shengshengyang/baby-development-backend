package com.dean.baby.common.dto;

import com.dean.baby.common.entity.Baby;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BabyDto {
    private String name;
    private String birthDate;
    private List<ProgressDto> progresses;

    public static BabyDto fromEntity(Baby baby) {
        return BabyDto.builder()
                .name(baby.getName())
                .birthDate(baby.getBirthDate().toString())
                .progresses(baby.getProgresses() == null ? null : ProgressDto.fromEntities(baby.getProgresses()))
                .build();
    }
}
