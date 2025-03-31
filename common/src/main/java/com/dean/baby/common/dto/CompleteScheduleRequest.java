package com.dean.baby.common.dto;

import java.time.LocalDate;

public record CompleteScheduleRequest(
        Long scheduleId,
        LocalDate actualDate
) {
}
