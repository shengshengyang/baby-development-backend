package com.dean.baby.common.dto;

import jakarta.annotation.Nullable;

public record BabyCreateRequestVo(
        @Nullable Long id,
        String name,
        String birthDate
) {
}
