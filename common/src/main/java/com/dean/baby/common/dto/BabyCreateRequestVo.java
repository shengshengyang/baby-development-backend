package com.dean.baby.common.dto;

public record BabyCreateRequestVo(
        Long id,
        String name,
        String birthDate
) {
}
