package com.dean.baby.common.dto;

import com.dean.baby.common.entity.Baby;
import com.dean.baby.common.entity.Category;
import com.dean.baby.common.entity.Flashcard;
import com.dean.baby.common.entity.Progress;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ProgressDto {
    private Baby baby;
    private Flashcard flashcard;
    private int ageInMonths;
    private Category category;
    private boolean achieved;
    private String dateAchieved;

    public static List<ProgressDto> fromEntities(List<Progress> progresses) {
        return progresses.stream()
                .map(progress -> ProgressDto.builder()
                        .baby(progress.getBaby())
                        .flashcard(progress.getFlashcard())
                        .ageInMonths(progress.getAgeInMonths())
                        .category(progress.getCategory())
                        .achieved(progress.isAchieved())
                        .dateAchieved(progress.getDateAchieved() != null ? progress.getDateAchieved().toString() : null)
                        .build())
                .toList();
    }
}
