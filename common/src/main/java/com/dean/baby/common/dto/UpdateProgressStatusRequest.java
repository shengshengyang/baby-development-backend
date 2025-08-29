package com.dean.baby.common.dto;

import com.dean.baby.common.entity.ProgressStatus;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Data
public class UpdateProgressStatusRequest {
    @NotNull(message = "babyId is required")
    private UUID babyId;

    @NotNull(message = "status is required")
    private ProgressStatus status;

    private UUID flashcardId;
    private UUID milestoneId;
    private UUID videoId;
    private Instant date;
}
