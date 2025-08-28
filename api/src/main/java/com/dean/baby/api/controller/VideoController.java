package com.dean.baby.api.controller;

import com.dean.baby.common.dto.VideoDto;
import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    /**
     * 創建關聯到milestone的video
     */
    @PostMapping("/milestone/{milestoneId}")
    public ResponseEntity<VideoDto> createVideoForMilestone(
            @PathVariable UUID milestoneId,
            @RequestBody VideoCreateRequest request) {

        VideoDto video = videoService.createVideoForMilestone(
                milestoneId,
                request.getDescription(),
                request.getVideoUrl(),
                request.getDurationSeconds(),
                request.getThumbnailUrl()
        );
        return new ResponseEntity<>(video, HttpStatus.CREATED);
    }

    /**
     * 創建關聯到article的video
     */
    @PostMapping("/article/{articleId}")
    public ResponseEntity<VideoDto> createVideoForArticle(
            @PathVariable UUID articleId,
            @RequestBody VideoCreateRequest request) {

        VideoDto video = videoService.createVideoForArticle(
                articleId,
                request.getDescription(),
                request.getVideoUrl(),
                request.getDurationSeconds(),
                request.getThumbnailUrl()
        );
        return new ResponseEntity<>(video, HttpStatus.CREATED);
    }

    /**
     * 創建關聯到flashcard的video
     */
    @PostMapping("/flashcard/{flashcardId}")
    public ResponseEntity<VideoDto> createVideoForFlashcard(
            @PathVariable UUID flashcardId,
            @RequestBody VideoCreateRequest request) {

        VideoDto video = videoService.createVideoForFlashcard(
                flashcardId,
                request.getDescription(),
                request.getVideoUrl(),
                request.getDurationSeconds(),
                request.getThumbnailUrl()
        );
        return new ResponseEntity<>(video, HttpStatus.CREATED);
    }

    /**
     * 獲取video by ID
     */
    @GetMapping("/{videoId}")
    public ResponseEntity<VideoDto> getVideoById(@PathVariable UUID videoId) {
        Optional<VideoDto> video = videoService.getVideoById(videoId);
        return video.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * 獲取milestone的所有videos
     */
    @GetMapping("/milestone/{milestoneId}")
    public ResponseEntity<List<VideoDto>> getVideosByMilestone(@PathVariable UUID milestoneId) {
        List<VideoDto> videos = videoService.getVideosByMilestone(milestoneId);
        return ResponseEntity.ok(videos);
    }

    /**
     * 獲取article的所有videos
     */
    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<VideoDto>> getVideosByArticle(@PathVariable UUID articleId) {
        List<VideoDto> videos = videoService.getVideosByArticle(articleId);
        return ResponseEntity.ok(videos);
    }

    /**
     * 獲取flashcard的所有videos
     */
    @GetMapping("/flashcard/{flashcardId}")
    public ResponseEntity<List<VideoDto>> getVideosByFlashcard(@PathVariable UUID flashcardId) {
        List<VideoDto> videos = videoService.getVideosByFlashcard(flashcardId);
        return ResponseEntity.ok(videos);
    }

    /**
     * 更新video
     */
    @PutMapping("/{videoId}")
    public ResponseEntity<VideoDto> updateVideo(
            @PathVariable UUID videoId,
            @RequestBody VideoCreateRequest request) {

        VideoDto video = videoService.updateVideo(
                videoId,
                request.getDescription(),
                request.getVideoUrl(),
                request.getDurationSeconds(),
                request.getThumbnailUrl()
        );
        return ResponseEntity.ok(video);
    }

    /**
     * 刪除video
     */
    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable UUID videoId) {
        videoService.deleteVideo(videoId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 獲取所有videos
     */
    @GetMapping
    public ResponseEntity<List<VideoDto>> getAllVideos() {
        List<VideoDto> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }

    /**
     * Video創建請求的DTO
     */
    public static class VideoCreateRequest {
        private LangFieldObject description;
        private String videoUrl;
        private Integer durationSeconds;
        private String thumbnailUrl;

        // Getters and Setters
        public LangFieldObject getDescription() { return description; }
        public void setDescription(LangFieldObject description) { this.description = description; }

        public String getVideoUrl() { return videoUrl; }
        public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

        public Integer getDurationSeconds() { return durationSeconds; }
        public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }

        public String getThumbnailUrl() { return thumbnailUrl; }
        public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }
    }
}
