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
    private UUID babyId;
    private UUID flashcardId;
    private UUID milestoneId;
    private UUID videoId;
    private ProgressType progressType;
    private ProgressStatus progressStatus;
    private UUID categoryId;
    private boolean achieved;
    private String dateAchieved;
    private String dateStarted;

    public static List<ProgressDto> fromEntities(List<Progress> progresses) {
        return progresses.stream()
                .map(ProgressDto::fromEntity)
                .toList();
    }

    // 單筆的fromEntity方法
    public static ProgressDto fromEntity(Progress progress) {
        return ProgressDto.builder()
                .id(progress.getId())
                .babyId(progress.getBaby() != null ? progress.getBaby().getId() : null)
                .flashcardId(progress.getFlashcard() != null ? progress.getFlashcard().getId() : null)
                .milestoneId(progress.getMilestone() != null ? progress.getMilestone().getId() : null)
                .videoId(progress.getVideo() != null ? progress.getVideo().getId() : null)
                .progressType(progress.getProgressType())
                .progressStatus(progress.getProgressStatus())
                .categoryId(progress.getCategory() != null ? progress.getCategory().getId() : null)
                .achieved(progress.isAchieved())
                .dateAchieved(progress.getDateAchieved() != null ? progress.getDateAchieved().toString() : null)
                .dateStarted(progress.getDateStarted() != null ? progress.getDateStarted().toString() : null)
                .build();
    }
}
