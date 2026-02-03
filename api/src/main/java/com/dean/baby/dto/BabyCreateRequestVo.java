package com.dean.baby.common.dto;

import jakarta.annotation.Nullable;

import java.util.UUID;

public record BabyCreateRequestVo(
        @Nullable UUID id,
        String name,
        String birthDate
) {
}
