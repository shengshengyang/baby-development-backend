package com.dean.baby.common.service;

import com.dean.baby.common.dto.ProgressDto;
import com.dean.baby.common.dto.UpdateProgressStatusRequest;
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

    // === 統一的進度管理方法 ===

    /**
     * 統一的進度狀態更新方法 - 直接處理 UpdateProgressStatusRequest
     */
    public ProgressDto updateProgressStatus(UpdateProgressStatusRequest request) {
        // 檢查至少要有一種 ID
        int idCount = 0;
        if (request.getFlashcardId() != null) idCount++;
        if (request.getMilestoneId() != null) idCount++;
        if (request.getVideoId() != null) idCount++;

        if (idCount == 0) {
            throw new ApiException(SysCode.BAD_REQUEST, "Must specify one of: flashcardId, milestoneId, or videoId");
        }
        if (idCount > 1) {
            throw new ApiException(SysCode.BAD_REQUEST, "Can only specify one of: flashcardId, milestoneId, or videoId");
        }

        // 處理時間轉換 - 將 Instant 轉為 LocalDate
        LocalDate actionDate = request.getDate() != null ?
            request.getDate().atZone(java.time.ZoneId.systemDefault()).toLocalDate() :
            LocalDate.now();

        Progress progress = findOrCreateProgress(request.getBabyId(), request.getFlashcardId(),
                                               request.getMilestoneId(), request.getVideoId());

        // 根據狀態決定操作
        switch (request.getStatus()) {
            case NOT_STARTED:
                progress.setProgressStatus(ProgressStatus.NOT_STARTED);
                progress.setDateStarted(null);
                progress.setDateAchieved(null);
                break;

            case IN_PROGRESS:
                progress.setProgressStatus(ProgressStatus.IN_PROGRESS);
                if (progress.getDateStarted() == null) {
                    progress.setDateStarted(actionDate);
                }
                progress.setDateAchieved(null);
                break;

            case COMPLETED:
                progress.setProgressStatus(ProgressStatus.COMPLETED);
                progress.setDateAchieved(actionDate);
                if (progress.getDateStarted() == null) {
                    progress.setDateStarted(actionDate);
                }
                break;
        }

        progress = progressRepository.save(progress);

        // 檢查是否該milestone也該標記為完成（僅當完成 flashcard 或 video 時）
        if (request.getStatus() == ProgressStatus.COMPLETED &&
            (request.getFlashcardId() != null || request.getVideoId() != null)) {
            checkAndMarkMilestoneCompleted(progress.getBaby(), progress);
        }

        // 檢查是否需要更新milestone狀態（當子項目被重置時）
        if (request.getStatus() == ProgressStatus.NOT_STARTED &&
            (request.getFlashcardId() != null || request.getVideoId() != null)) {
            checkAndUpdateMilestoneAfterReset(progress.getBaby(), progress);
        }

        return ProgressDto.fromEntity(progress);
    }


    /**
     * 更新進度狀態
     */
    public ProgressDto updateProgressStatus(UUID progressId, ProgressStatus status, LocalDate actionDate) {
        Progress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Progress not found"));

        progress.setProgressStatus(status);

        switch (status) {
            case NOT_STARTED:
                progress.setDateStarted(null);
                progress.setDateAchieved(null);
                break;
            case IN_PROGRESS:
                if (progress.getDateStarted() == null) {
                    progress.setDateStarted(actionDate);
                }
                progress.setDateAchieved(null);
                break;
            case COMPLETED:
                progress.setDateAchieved(actionDate);
                if (progress.getDateStarted() == null) {
                    progress.setDateStarted(actionDate);
                }
                break;
        }

        progress = progressRepository.save(progress);
        return ProgressDto.fromEntity(progress);
    }

    /**
     * 刪除進度記錄
     */
    public void deleteProgress(UUID progressId) {
        Progress progress = progressRepository.findById(progressId)
                .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Progress not found"));
        progressRepository.delete(progress);
    }

    // === 查詢方法 ===

    /**
     * 獲取baby的所有進度，支援篩選條件
     */
    @Transactional(readOnly = true)
    public List<ProgressDto> getBabyProgress(UUID babyId, ProgressStatus status, ProgressType type) {
        List<Progress> progresses;

        if (status != null && type != null) {
            progresses = progressRepository.findByBabyIdAndProgressTypeAndProgressStatus(babyId, type, status);
        } else if (status != null) {
            progresses = progressRepository.findByBabyIdAndProgressStatus(babyId, status);
        } else if (type != null) {
            progresses = progressRepository.findByBabyIdAndProgressType(babyId, type);
        } else {
            progresses = progressRepository.findByBabyId(babyId);
        }

        return ProgressDto.fromEntities(progresses);
    }

    /**
     * 根據進度狀態獲取baby的進度
     */
    @Transactional(readOnly = true)
    public List<ProgressDto> getBabyProgressByStatus(UUID babyId, ProgressStatus status) {
        List<Progress> progresses = progressRepository.findByBabyIdAndProgressStatus(babyId, status);
        return ProgressDto.fromEntities(progresses);
    }

    /**
     * 根據進度類型和狀態獲取baby的進度
     */
    @Transactional(readOnly = true)
    public List<ProgressDto> getBabyProgressByTypeAndStatus(UUID babyId, ProgressType type, ProgressStatus status) {
        List<Progress> progresses = progressRepository.findByBabyIdAndProgressTypeAndProgressStatus(babyId, type, status);
        return ProgressDto.fromEntities(progresses);
    }

    // === 私有輔助方法 ===

    /**
     * 尋找或創建進度記錄
     */
    private Progress findOrCreateProgress(UUID babyId, UUID flashcardId, UUID milestoneId, UUID videoId) {
        Optional<Progress> existingProgress = findExistingProgress(babyId, flashcardId, milestoneId, videoId);

        if (existingProgress.isPresent()) {
            return existingProgress.get();
        }

        // 創建新記錄
        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Baby not found"));

        Progress.ProgressBuilder builder = Progress.builder()
                .baby(baby)
                .progressStatus(ProgressStatus.NOT_STARTED);

        if (flashcardId != null) {
            Flashcard flashcard = flashcardRepository.findById(flashcardId)
                    .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Flashcard not found"));
            builder.flashcard(flashcard)
                   .milestone(flashcard.getMilestone())
                   .progressType(ProgressType.FLASHCARD);
        } else if (milestoneId != null) {
            Milestone milestone = milestoneRepository.findById(milestoneId)
                    .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Milestone not found"));
            builder.milestone(milestone)
                   .progressType(ProgressType.MILESTONE);
        } else if (videoId != null) {
            Video video = videoRepository.findById(videoId)
                    .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND, "Video not found"));
            builder.video(video)
                   .milestone(video.getMilestone())
                   .progressType(ProgressType.VIDEO);
        } else {
            throw new ApiException(SysCode.BAD_REQUEST, "Must specify flashcardId, milestoneId, or videoId");
        }

        return builder.build();
    }

    /**
     * 尋找現有的進度記錄
     */
    private Optional<Progress> findExistingProgress(UUID babyId, UUID flashcardId, UUID milestoneId, UUID videoId) {
        if (flashcardId != null) {
            return progressRepository.findByBabyIdAndFlashcardIdAndProgressType(babyId, flashcardId, ProgressType.FLASHCARD);
        } else if (milestoneId != null) {
            return progressRepository.findByBabyIdAndMilestoneIdAndProgressType(babyId, milestoneId, ProgressType.MILESTONE);
        } else if (videoId != null) {
            return progressRepository.findByBabyIdAndVideoIdAndProgressType(babyId, videoId, ProgressType.VIDEO);
        }
        return Optional.empty();
    }

    /**
     * 檢查並自動標記milestone完成
     */
    private void checkAndMarkMilestoneCompleted(Baby baby, Progress triggerProgress) {
        Milestone milestone = triggerProgress.getMilestone();
        if (milestone == null) return;

        // 獲取該milestone下的所有flashcards和videos
        List<Flashcard> milestoneFlashcards = flashcardRepository.findByMilestoneId(milestone.getId());
        List<Video> milestoneVideos = videoRepository.findByMilestoneId(milestone.getId());

        // 檢查是否所有相關項目都已完成
        boolean allCompleted = true;

        for (Flashcard flashcard : milestoneFlashcards) {
            Optional<Progress> progress = progressRepository.findByBabyIdAndFlashcardIdAndProgressType(
                    baby.getId(), flashcard.getId(), ProgressType.FLASHCARD);
            if (progress.isEmpty() || progress.get().getProgressStatus() != ProgressStatus.COMPLETED) {
                allCompleted = false;
                break;
            }
        }

        if (allCompleted) {
            for (Video video : milestoneVideos) {
                Optional<Progress> progress = progressRepository.findByBabyIdAndVideoIdAndProgressType(
                        baby.getId(), video.getId(), ProgressType.VIDEO);
                if (progress.isEmpty() || progress.get().getProgressStatus() != ProgressStatus.COMPLETED) {
                    allCompleted = false;
                    break;
                }
            }
        }

        // 如果所有項目都完成，自動標記milestone為完成
        if (allCompleted && (!milestoneFlashcards.isEmpty() || !milestoneVideos.isEmpty())) {
            Optional<Progress> milestoneProgress = progressRepository.findByBabyIdAndMilestoneIdAndProgressType(
                    baby.getId(), milestone.getId(), ProgressType.MILESTONE);

            if (milestoneProgress.isEmpty() || milestoneProgress.get().getProgressStatus() != ProgressStatus.COMPLETED) {
                // 創建 UpdateProgressStatusRequest 來完成 milestone
                UpdateProgressStatusRequest milestoneRequest = new UpdateProgressStatusRequest();
                milestoneRequest.setBabyId(baby.getId());
                milestoneRequest.setMilestoneId(milestone.getId());
                milestoneRequest.setStatus(ProgressStatus.COMPLETED);
                milestoneRequest.setDate(LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
                updateProgressStatus(milestoneRequest);
            }
        }
    }

    /**
     * 檢查並更新milestone狀態（當子項目被重置時）
     */
    private void checkAndUpdateMilestoneAfterReset(Baby baby, Progress resetProgress) {
        Milestone milestone = resetProgress.getMilestone();
        if (milestone == null) return;

        // 如果milestone目前是完成狀態，檢查是否需要取消完成狀態
        Optional<Progress> milestoneProgress = progressRepository.findByBabyIdAndMilestoneIdAndProgressType(
                baby.getId(), milestone.getId(), ProgressType.MILESTONE);

        if (milestoneProgress.isPresent() && milestoneProgress.get().getProgressStatus() == ProgressStatus.COMPLETED) {
            // 將milestone狀態改為進行中
            updateProgressStatus(milestoneProgress.get().getId(), ProgressStatus.IN_PROGRESS, LocalDate.now());
        }
    }
}
