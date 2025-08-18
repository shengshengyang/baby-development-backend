package com.dean.baby.common.dto;

import java.time.LocalDate;
import java.util.UUID;

public record CompleteScheduleRequest(
        UUID scheduleId,
        LocalDate actualDate
) {
}
