package com.dean.baby.dto;

public record BabyCreateRequestVo(
        Long id,
        String name,
        String birthDate
) {
}
