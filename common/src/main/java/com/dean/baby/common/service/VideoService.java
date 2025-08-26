package com.dean.baby.common.service;

import com.dean.baby.common.dto.VideoDto;
import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.entity.*;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class VideoService extends BaseService {

    private final VideoRepository videoRepository;
    private final MilestoneRepository milestoneRepository;
    private final ArticleRepository articleRepository;
    private final FlashcardRepository flashcardRepository;

    @Autowired
    public VideoService(UserRepository userRepository,
                       VideoRepository videoRepository,
                       MilestoneRepository milestoneRepository,
                       ArticleRepository articleRepository,
                       FlashcardRepository flashcardRepository) {
        super(userRepository);
        this.videoRepository = videoRepository;
        this.milestoneRepository = milestoneRepository;
        this.articleRepository = articleRepository;
        this.flashcardRepository = flashcardRepository;
    }

    /**
     * 創建關聯到milestone的video
     */
    public VideoDto createVideoForMilestone(UUID milestoneId, LangFieldObject description,
                                          String videoUrl, Integer durationSeconds, String thumbnailUrl) {
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Milestone not found"));

        Video video = Video.builder()
                .description(description)
                .videoUrl(videoUrl)
                .durationSeconds(durationSeconds)
                .thumbnailUrl(thumbnailUrl)
                .milestone(milestone)
                .build();

        video = videoRepository.save(video);
        return VideoDto.fromEntity(video);
    }

    /**
     * 創建關聯到article的video
     */
    public VideoDto createVideoForArticle(UUID articleId, LangFieldObject description,
                                        String videoUrl, Integer durationSeconds, String thumbnailUrl) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Article not found"));

        Video video = Video.builder()
                .description(description)
                .videoUrl(videoUrl)
                .durationSeconds(durationSeconds)
                .thumbnailUrl(thumbnailUrl)
                .article(article)
                .build();

        video = videoRepository.save(video);
        return VideoDto.fromEntity(video);
    }

    /**
     * 創建關聯到flashcard的video
     */
    public VideoDto createVideoForFlashcard(UUID flashcardId, LangFieldObject description,
                                          String videoUrl, Integer durationSeconds, String thumbnailUrl) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Flashcard not found"));

        Video video = Video.builder()
                .description(description)
                .videoUrl(videoUrl)
                .durationSeconds(durationSeconds)
                .thumbnailUrl(thumbnailUrl)
                .flashcard(flashcard)
                .milestone(flashcard.getMilestone()) // 自動關聯到flashcard的milestone
                .build();

        video = videoRepository.save(video);
        return VideoDto.fromEntity(video);
    }

    /**
     * 獲取video by ID
     */
    @Transactional(readOnly = true)
    public Optional<VideoDto> getVideoById(UUID videoId) {
        return videoRepository.findById(videoId)
                .map(VideoDto::fromEntity);
    }

    /**
     * 獲取milestone的所有videos
     */
    @Transactional(readOnly = true)
    public List<VideoDto> getVideosByMilestone(UUID milestoneId) {
        List<Video> videos = videoRepository.findByMilestoneId(milestoneId);
        return VideoDto.fromEntities(videos);
    }

    /**
     * 獲取article的所有videos
     */
    @Transactional(readOnly = true)
    public List<VideoDto> getVideosByArticle(UUID articleId) {
        List<Video> videos = videoRepository.findByArticleId(articleId);
        return VideoDto.fromEntities(videos);
    }

    /**
     * 獲取flashcard的所有videos
     */
    @Transactional(readOnly = true)
    public List<VideoDto> getVideosByFlashcard(UUID flashcardId) {
        List<Video> videos = videoRepository.findByFlashcardId(flashcardId);
        return VideoDto.fromEntities(videos);
    }

    /**
     * 更新video
     */
    public VideoDto updateVideo(UUID videoId, LangFieldObject description,
                              String videoUrl, Integer durationSeconds, String thumbnailUrl) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Video not found"));

        video.setDescription(description);
        video.setVideoUrl(videoUrl);
        video.setDurationSeconds(durationSeconds);
        video.setThumbnailUrl(thumbnailUrl);

        video = videoRepository.save(video);
        return VideoDto.fromEntity(video);
    }

    /**
     * 刪除video
     */
    public void deleteVideo(UUID videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Video not found"));

        videoRepository.delete(video);
    }

    /**
     * 獲取所有videos
     */
    @Transactional(readOnly = true)
    public List<VideoDto> getAllVideos() {
        List<Video> videos = videoRepository.findAll();
        return VideoDto.fromEntities(videos);
    }
}
