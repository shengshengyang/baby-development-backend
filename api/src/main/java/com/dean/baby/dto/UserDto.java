package com.dean.baby.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
    private String username;
    private String email;
    private List<String> role;
    private String token;
    private List<BabyDto> babies;
}
