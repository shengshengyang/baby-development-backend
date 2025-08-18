package com.dean.baby.common.dto;

import java.util.UUID;

public record CheckProgressRequestVo(
        UUID babyId,
        UUID flashcardId
) {
}
