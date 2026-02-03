package com.dean.baby.common.dto;

public record UpdateUserRequestVo(
        String email,
        String password,
        UpdateBabyDto baby
) {
}
