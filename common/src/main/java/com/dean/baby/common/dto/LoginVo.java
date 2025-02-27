package com.dean.baby.common.dto;

import lombok.Builder;

@Builder
public record LoginVo(String email, String password) {
}
