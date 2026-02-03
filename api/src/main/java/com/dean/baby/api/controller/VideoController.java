package com.dean.baby.api.controller;

import com.dean.baby.common.dto.VideoDto;
import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/videos")
@Tag(name = "影片管理", description = "教學影片管理 API")
public class VideoController {

    private final VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @Operation(summary = "為里程碑創建影片", description = "創建關聯到指定里程碑的教學影片")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "創建成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "里程碑不存在")
    })
    @PostMapping("/milestone/{milestoneId}")
    public ResponseEntity<VideoDto> createVideoForMilestone(
            @Parameter(description = "里程碑 ID") @PathVariable UUID milestoneId,
            @RequestBody VideoCreateRequest request) {

        VideoDto video = videoService.createVideoForMilestone(
                milestoneId,
                request.getDescription(),
                request.getVideoUrl(),
                request.getDurationSeconds(),
                request.getThumbnailUrl());
        return new ResponseEntity<>(video, HttpStatus.CREATED);
    }

    @Operation(summary = "為文章創建影片", description = "創建關聯到指定文章的教學影片")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "創建成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "文章不存在")
    })
    @PostMapping("/article/{articleId}")
    public ResponseEntity<VideoDto> createVideoForArticle(
            @Parameter(description = "文章 ID") @PathVariable UUID articleId,
            @RequestBody VideoCreateRequest request) {

        VideoDto video = videoService.createVideoForArticle(
                articleId,
                request.getDescription(),
                request.getVideoUrl(),
                request.getDurationSeconds(),
                request.getThumbnailUrl());
        return new ResponseEntity<>(video, HttpStatus.CREATED);
    }

    @Operation(summary = "為閃卡創建影片", description = "創建關聯到指定閃卡的教學影片")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "創建成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "閃卡不存在")
    })
    @PostMapping("/flashcard/{flashcardId}")
    public ResponseEntity<VideoDto> createVideoForFlashcard(
            @Parameter(description = "閃卡 ID") @PathVariable UUID flashcardId,
            @RequestBody VideoCreateRequest request) {

        VideoDto video = videoService.createVideoForFlashcard(
                flashcardId,
                request.getDescription(),
                request.getVideoUrl(),
                request.getDurationSeconds(),
                request.getThumbnailUrl());
        return new ResponseEntity<>(video, HttpStatus.CREATED);
    }

    @Operation(summary = "獲取影片詳情", description = "根據 ID 獲取單個影片的詳細信息")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "獲取成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "影片不存在")
    })
    @GetMapping("/{videoId}")
    public ResponseEntity<VideoDto> getVideoById(
            @Parameter(description = "影片 ID") @PathVariable UUID videoId) {
        Optional<VideoDto> video = videoService.getVideoById(videoId);
        return video.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "獲取里程碑的所有影片", description = "獲取關聯到指定里程碑的所有影片")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "獲取成功")
    })
    @GetMapping("/milestone/{milestoneId}")
    public ResponseEntity<List<VideoDto>> getVideosByMilestone(
            @Parameter(description = "里程碑 ID") @PathVariable UUID milestoneId) {
        List<VideoDto> videos = videoService.getVideosByMilestone(milestoneId);
        return ResponseEntity.ok(videos);
    }

    @Operation(summary = "獲取文章的所有影片", description = "獲取關聯到指定文章的所有影片")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "獲取成功")
    })
    @GetMapping("/article/{articleId}")
    public ResponseEntity<List<VideoDto>> getVideosByArticle(
            @Parameter(description = "文章 ID") @PathVariable UUID articleId) {
        List<VideoDto> videos = videoService.getVideosByArticle(articleId);
        return ResponseEntity.ok(videos);
    }

    @Operation(summary = "獲取閃卡的所有影片", description = "獲取關聯到指定閃卡的所有影片")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "獲取成功")
    })
    @GetMapping("/flashcard/{flashcardId}")
    public ResponseEntity<List<VideoDto>> getVideosByFlashcard(
            @Parameter(description = "閃卡 ID") @PathVariable UUID flashcardId) {
        List<VideoDto> videos = videoService.getVideosByFlashcard(flashcardId);
        return ResponseEntity.ok(videos);
    }

    @Operation(summary = "更新影片", description = "更新指定 ID 的影片信息")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "更新成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "影片不存在")
    })
    @PutMapping("/{videoId}")
    public ResponseEntity<VideoDto> updateVideo(
            @Parameter(description = "影片 ID") @PathVariable UUID videoId,
            @RequestBody VideoCreateRequest request) {

        VideoDto video = videoService.updateVideo(
                videoId,
                request.getDescription(),
                request.getVideoUrl(),
                request.getDurationSeconds(),
                request.getThumbnailUrl());
        return ResponseEntity.ok(video);
    }

    @Operation(summary = "刪除影片", description = "刪除指定 ID 的影片")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "刪除成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "未授權")
    })
    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> deleteVideo(
            @Parameter(description = "影片 ID") @PathVariable UUID videoId) {
        videoService.deleteVideo(videoId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "分頁獲取所有影片", description = "分頁獲取所有影片列表")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "獲取成功")
    })
    @GetMapping("/page")
    public ResponseEntity<Page<VideoDto>> getAllVideosPaginated(
            @org.springframework.data.web.PageableDefault(size = 12, sort = "id") org.springframework.data.domain.Pageable pageable) {
        Page<VideoDto> videoPage = videoService.getAllVideosPaginated(pageable);
        return ResponseEntity.ok(videoPage);
    }

    @Operation(summary = "獲取所有影片", description = "獲取所有影片列表（不分頁）")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "獲取成功")
    })
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
        public LangFieldObject getDescription() {
            return description;
        }

        public void setDescription(LangFieldObject description) {
            this.description = description;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public Integer getDurationSeconds() {
            return durationSeconds;
        }

        public void setDurationSeconds(Integer durationSeconds) {
            this.durationSeconds = durationSeconds;
        }

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }
    }
}
