package com.dean.baby.common.dto;

import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.entity.Article;
import com.dean.baby.common.entity.Flashcard;
import com.dean.baby.common.entity.Milestone;
import com.dean.baby.common.entity.Video;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class VideoDto {
    private UUID id;
    private LangFieldObject description;
    private String videoUrl;
    private Integer durationSeconds;
    private String thumbnailUrl;
    private MilestoneDTO milestone;
    private ArticalDto article;
    private FlashcardDTO flashcard;

    public static List<VideoDto> fromEntities(List<Video> videos) {
        return videos.stream()
                .map(VideoDto::fromEntity)
                .toList();
    }

    public static VideoDto fromEntity(Video video) {
        return VideoDto.builder()
                .id(video.getId())
                .description(video.getDescription())
                .videoUrl(video.getVideoUrl())
                .durationSeconds(video.getDurationSeconds())
                .thumbnailUrl(video.getThumbnailUrl())
                .milestone(MilestoneDTO.fromEntity(video.getMilestone()))
                .article(ArticalDto.fromEntity(video.getArticle()))
                .flashcard(FlashcardDTO.fromEntity(video.getFlashcard()))
                .build();
    }
}
