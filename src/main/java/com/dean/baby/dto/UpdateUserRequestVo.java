package com.dean.baby.dto;

public record UpdateUserRequestVo(
        String email,
        String password,
        UpdateBabyDto baby
) {
}
