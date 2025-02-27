package com.dean.baby.common.dto;

import com.dean.baby.common.entity.Progress;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProgressDto {
    private int babyId;
    private int flashcardId;
    private int ageInMonths;
    private String category;
    private boolean achieved;
    private String dateAchieved;

    public static List<ProgressDto> fromEntities(List<Progress> progresses) {
        return progresses.stream()
                .map(progress -> ProgressDto.builder()
                        .babyId(progress.getBaby().getId().intValue())
                        .flashcardId(progress.getFlashcard().getId().intValue())
                        .ageInMonths(progress.getAgeInMonths())
                        .category(progress.getCategory())
                        .achieved(progress.isAchieved())
                        .dateAchieved(progress.getDateAchieved().toString())
                        .build())
                .toList();
    }
}
