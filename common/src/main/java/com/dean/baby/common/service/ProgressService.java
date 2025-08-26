package com.dean.baby.common.service;

import com.dean.baby.common.dto.ProgressDto;
import com.dean.baby.common.entity.*;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ProgressService extends BaseService {

    private final ProgressRepository progressRepository;
    private final BabyRepository babyRepository;
    private final FlashcardRepository flashcardRepository;
    private final MilestoneRepository milestoneRepository;
    private final VideoRepository videoRepository;

    @Autowired
    public ProgressService(UserRepository userRepository,
                          ProgressRepository progressRepository,
                          BabyRepository babyRepository,
                          FlashcardRepository flashcardRepository,
                          MilestoneRepository milestoneRepository,
                          VideoRepository videoRepository) {
        super(userRepository);
        this.progressRepository = progressRepository;
        this.babyRepository = babyRepository;
        this.flashcardRepository = flashcardRepository;
        this.milestoneRepository = milestoneRepository;
        this.videoRepository = videoRepository;
    }

    /**
     * 標記baby完成了某個flashcard
     */
    public ProgressDto markFlashcardCompleted(UUID babyId, UUID flashcardId, LocalDate dateAchieved) {
        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Baby not found"));

        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Flashcard not found"));

        // 檢查是否已存在記錄
        Optional<Progress> existingProgress = progressRepository.findByBabyIdAndFlashcardIdAndProgressType(
                babyId, flashcardId, ProgressType.FLASHCARD);

        Progress progress;
        if (existingProgress.isPresent()) {
            // 更新現有記錄
            progress = existingProgress.get();
            progress.setAchieved(true);
            progress.setDateAchieved(dateAchieved);
        } else {
            // 創建新記錄
            progress = Progress.builder()
                    .baby(baby)
                    .flashcard(flashcard)
                    .milestone(flashcard.getMilestone())
                    .progressType(ProgressType.FLASHCARD)
                    .achieved(true)
                    .dateAchieved(dateAchieved)
                    .build();
        }

        progress = progressRepository.save(progress);

        // 檢查是否該milestone也該標記為完成
        checkAndMarkMilestoneCompleted(baby, flashcard.getMilestone());

        return ProgressDto.fromEntity(progress);
    }

    /**
     * 標記baby達成了某個milestone
     */
    public ProgressDto markMilestoneCompleted(UUID babyId, UUID milestoneId, LocalDate dateAchieved) {
        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Baby not found"));

        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Milestone not found"));

        // 檢查是否已存在記錄
        Optional<Progress> existingProgress = progressRepository.findByBabyIdAndMilestoneIdAndProgressType(
                babyId, milestoneId, ProgressType.MILESTONE);

        Progress progress;
        if (existingProgress.isPresent()) {
            // 更新現有記錄
            progress = existingProgress.get();
            progress.setAchieved(true);
            progress.setDateAchieved(dateAchieved);
        } else {
            // 創建新記錄
            progress = Progress.builder()
                    .baby(baby)
                    .milestone(milestone)
                    .progressType(ProgressType.MILESTONE)
                    .achieved(true)
                    .dateAchieved(dateAchieved)
                    .build();
        }

        return ProgressDto.fromEntity(progressRepository.save(progress));
    }

    /**
     * 標記baby完成了某個video
     */
    public ProgressDto markVideoCompleted(UUID babyId, UUID videoId, LocalDate dateAchieved) {
        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Baby not found"));

        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Video not found"));

        // 檢查是否已存在記錄
        Optional<Progress> existingProgress = progressRepository.findByBabyIdAndVideoIdAndProgressType(
                babyId, videoId, ProgressType.VIDEO);

        Progress progress;
        if (existingProgress.isPresent()) {
            // 更新現有記錄
            progress = existingProgress.get();
            progress.setAchieved(true);
            progress.setDateAchieved(dateAchieved);
        } else {
            // 創建新記錄
            progress = Progress.builder()
                    .baby(baby)
                    .video(video)
                    .milestone(video.getMilestone())
                    .progressType(ProgressType.VIDEO)
                    .achieved(true)
                    .dateAchieved(dateAchieved)
                    .build();
        }

        progress = progressRepository.save(progress);

        // 檢查是否該milestone也該標記為完成
        if (video.getMilestone() != null) {
            checkAndMarkMilestoneCompleted(baby, video.getMilestone());
        }

        return ProgressDto.fromEntity(progress);
    }

    /**
     * 檢查並自動標記milestone完成（當所有相關flashcard和video都完成時）
     */
    private void checkAndMarkMilestoneCompleted(Baby baby, Milestone milestone) {
        // 獲取該milestone下的所有flashcards
        List<Flashcard> milestoneFlashcards = flashcardRepository.findByMilestoneId(milestone.getId());

        // 獲取該milestone下的所有videos
        List<Video> milestoneVideos = videoRepository.findByMilestoneId(milestone.getId());

        // 檢查baby是否完成了所有相關flashcards
        long completedFlashcards = milestoneFlashcards.stream()
                .mapToLong(flashcard ->
                    progressRepository.findByBabyIdAndFlashcardIdAndProgressType(
                        baby.getId(), flashcard.getId(), ProgressType.FLASHCARD)
                        .filter(Progress::isAchieved)
                        .isPresent() ? 1 : 0)
                .sum();

        // 檢查baby是否完成了所有相關videos
        long completedVideos = milestoneVideos.stream()
                .mapToLong(video ->
                    progressRepository.findByBabyIdAndVideoIdAndProgressType(
                        baby.getId(), video.getId(), ProgressType.VIDEO)
                        .filter(Progress::isAchieved)
                        .isPresent() ? 1 : 0)
                .sum();

        // 只有當所有flashcards和videos都完成時，才標記milestone為完成
        boolean allFlashcardsCompleted = completedFlashcards == milestoneFlashcards.size();
        boolean allVideosCompleted = completedVideos == milestoneVideos.size();
        boolean hasContent = !milestoneFlashcards.isEmpty() || !milestoneVideos.isEmpty();

        if (allFlashcardsCompleted && allVideosCompleted && hasContent) {
            // 檢查milestone是否已標記完成
            Optional<Progress> milestoneProgress = progressRepository.findByBabyIdAndMilestoneIdAndProgressType(
                    baby.getId(), milestone.getId(), ProgressType.MILESTONE);

            if (milestoneProgress.isEmpty()) {
                Progress newMilestoneProgress = Progress.builder()
                        .baby(baby)
                        .milestone(milestone)
                        .progressType(ProgressType.MILESTONE)
                        .achieved(true)
                        .dateAchieved(LocalDate.now())
                        .build();
                progressRepository.save(newMilestoneProgress);
            }
        }
    }

    /**
     * 獲取baby的所有進度
     */
    @Transactional(readOnly = true)
    public List<ProgressDto> getBabyProgress(UUID babyId) {
        List<Progress> progresses = progressRepository.findByBabyId(babyId);
        return ProgressDto.fromEntities(progresses);
    }

    /**
     * 獲取baby完成的flashcards
     */
    @Transactional(readOnly = true)
    public List<ProgressDto> getBabyCompletedFlashcards(UUID babyId) {
        List<Progress> progresses = progressRepository.findByBabyIdAndProgressTypeAndAchieved(
                babyId, ProgressType.FLASHCARD, true);
        return ProgressDto.fromEntities(progresses);
    }

    /**
     * 獲取baby達成的milestones
     */
    @Transactional(readOnly = true)
    public List<ProgressDto> getBabyCompletedMilestones(UUID babyId) {
        List<Progress> progresses = progressRepository.findByBabyIdAndProgressTypeAndAchieved(
                babyId, ProgressType.MILESTONE, true);
        return ProgressDto.fromEntities(progresses);
    }

    /**
     * 獲取baby完成的videos
     */
    @Transactional(readOnly = true)
    public List<ProgressDto> getBabyCompletedVideos(UUID babyId) {
        List<Progress> progresses = progressRepository.findByBabyIdAndProgressTypeAndAchieved(
                babyId, ProgressType.VIDEO, true);
        return ProgressDto.fromEntities(progresses);
    }

    /**
     * 取消flashcard完成狀態
     */
    public void unmarkFlashcardCompleted(UUID babyId, UUID flashcardId) {
        Optional<Progress> progress = progressRepository.findByBabyIdAndFlashcardIdAndProgressType(
                babyId, flashcardId, ProgressType.FLASHCARD);

        progress.ifPresent(p -> {
            p.setAchieved(false);
            p.setDateAchieved(null);
            progressRepository.save(p);

            // 檢查是否需要取消milestone完成狀態
            Flashcard flashcard = flashcardRepository.findById(flashcardId).orElse(null);
            if (flashcard != null) {
                checkAndUnmarkMilestoneCompleted(babyRepository.findById(babyId).orElse(null),
                                               flashcard.getMilestone());
            }
        });
    }

    /**
     * 取消video完成狀態
     */
    public void unmarkVideoCompleted(UUID babyId, UUID videoId) {
        Optional<Progress> progress = progressRepository.findByBabyIdAndVideoIdAndProgressType(
                babyId, videoId, ProgressType.VIDEO);

        progress.ifPresent(p -> {
            p.setAchieved(false);
            p.setDateAchieved(null);
            progressRepository.save(p);

            // 檢查是否需要取消milestone完成狀態
            Video video = videoRepository.findById(videoId).orElse(null);
            if (video != null && video.getMilestone() != null) {
                checkAndUnmarkMilestoneCompleted(babyRepository.findById(babyId).orElse(null),
                                               video.getMilestone());
            }
        });
    }

    /**
     * 檢查並取消milestone完成狀態（當有flashcard或video被取消完成時）
     */
    private void checkAndUnmarkMilestoneCompleted(Baby baby, Milestone milestone) {
        if (baby == null || milestone == null) return;

        Optional<Progress> milestoneProgress = progressRepository.findByBabyIdAndMilestoneIdAndProgressType(
                baby.getId(), milestone.getId(), ProgressType.MILESTONE);

        if (milestoneProgress.isPresent() && milestoneProgress.get().isAchieved()) {
            milestoneProgress.get().setAchieved(false);
            milestoneProgress.get().setDateAchieved(null);
            progressRepository.save(milestoneProgress.get());
        }
    }

    /**
     * 刪除進度記錄
     */
    public void deleteProgress(UUID progressId) {
        Progress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Progress not found"));

        progressRepository.delete(progress);
    }
}
