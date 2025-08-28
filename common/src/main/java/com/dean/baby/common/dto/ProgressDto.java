package com.dean.baby.common.dto;

import com.dean.baby.common.entity.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ProgressDto {
    private UUID id;
    private Baby baby;
    private Flashcard flashcard;
    private Milestone milestone;
    private Video video;
    private ProgressType progressType;
    private int ageInMonths;
    private Category category;
    private boolean achieved;
    private String dateAchieved;

    public static List<ProgressDto> fromEntities(List<Progress> progresses) {
        return progresses.stream()
                .map(progress -> ProgressDto.builder()
                        .id(progress.getId())
                        .baby(progress.getBaby())
                        .flashcard(progress.getFlashcard())
                        .milestone(progress.getMilestone())
                        .video(progress.getVideo())
                        .progressType(progress.getProgressType())
                        .ageInMonths(progress.getAgeInMonths())
                        .category(progress.getCategory())
                        .achieved(progress.isAchieved())
                        .dateAchieved(progress.getDateAchieved() != null ? progress.getDateAchieved().toString() : null)
                        .build())
                .toList();
    }

    public static ProgressDto fromEntity(Progress progress) {
        return ProgressDto.builder()
                .id(progress.getId())
                .baby(progress.getBaby())
                .flashcard(progress.getFlashcard())
                .milestone(progress.getMilestone())
                .video(progress.getVideo())
                .progressType(progress.getProgressType())
                .ageInMonths(progress.getAgeInMonths())
                .category(progress.getCategory())
                .achieved(progress.isAchieved())
                .dateAchieved(progress.getDateAchieved() != null ? progress.getDateAchieved().toString() : null)
                .build();
    }
}
