package com.dean.baby.dto;

import java.util.UUID;

public record CheckProgressRequestVo(
        UUID babyId,
        UUID flashcardId
) {
}
