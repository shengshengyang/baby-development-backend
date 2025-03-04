package com.dean.baby.dto;

import lombok.Builder;

@Builder
public record LoginVo(String email, String password) {
}
