package com.dean.baby.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateBabyDto {
    private String name;
    private Double headCircumference;

    private Double height;

    private Double weight;

}
