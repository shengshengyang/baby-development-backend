package com.dean.baby.dto;

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
}
