package com.dean.baby.dto;

import com.dean.baby.entity.Baby;
import com.dean.baby.entity.Progress;
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
                .progresses(ProgressDto.fromEntities(baby.getProgresses()))
                .build();
    }
}
