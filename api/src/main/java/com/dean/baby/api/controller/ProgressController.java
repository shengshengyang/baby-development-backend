package com.dean.baby.api.controller;

import com.dean.baby.common.dto.ProgressDto;
import com.dean.baby.common.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    private final ProgressService progressService;

    @Autowired
    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    /**
     * 標記baby完成某個flashcard
     */
    @PostMapping("/flashcard/complete")
    public ResponseEntity<ProgressDto> markFlashcardCompleted(
            @RequestParam UUID babyId,
            @RequestParam UUID flashcardId,
            @RequestParam(required = false) String dateAchieved) {

        LocalDate date = dateAchieved != null ? LocalDate.parse(dateAchieved) : LocalDate.now();
        ProgressDto progress = progressService.markFlashcardCompleted(babyId, flashcardId, date);
        return new ResponseEntity<>(progress, HttpStatus.CREATED);
    }

    /**
     * 標記baby達成某個milestone
     */
    @PostMapping("/milestone/complete")
    public ResponseEntity<ProgressDto> markMilestoneCompleted(
            @RequestParam UUID babyId,
            @RequestParam UUID milestoneId,
            @RequestParam(required = false) String dateAchieved) {

        LocalDate date = dateAchieved != null ? LocalDate.parse(dateAchieved) : LocalDate.now();
        ProgressDto progress = progressService.markMilestoneCompleted(babyId, milestoneId, date);
        return new ResponseEntity<>(progress, HttpStatus.CREATED);
    }

    /**
     * 取消flashcard完成狀態
     */
    @PostMapping("/flashcard/uncomplete")
    public ResponseEntity<Void> unmarkFlashcardCompleted(
            @RequestParam UUID babyId,
            @RequestParam UUID flashcardId) {

        progressService.unmarkFlashcardCompleted(babyId, flashcardId);
        return ResponseEntity.ok().build();
    }

    /**
     * 標記baby完成某個video
     */
    @PostMapping("/video/complete")
    public ResponseEntity<ProgressDto> markVideoCompleted(
            @RequestParam UUID babyId,
            @RequestParam UUID videoId,
            @RequestParam(required = false) String dateAchieved) {

        LocalDate date = dateAchieved != null ? LocalDate.parse(dateAchieved) : LocalDate.now();
        ProgressDto progress = progressService.markVideoCompleted(babyId, videoId, date);
        return new ResponseEntity<>(progress, HttpStatus.CREATED);
    }

    /**
     * 取消video完成狀態
     */
    @PostMapping("/video/uncomplete")
    public ResponseEntity<Void> unmarkVideoCompleted(
            @RequestParam UUID babyId,
            @RequestParam UUID videoId) {

        progressService.unmarkVideoCompleted(babyId, videoId);
        return ResponseEntity.ok().build();
    }

    /**
     * 獲取baby的所有進度
     */
    @GetMapping("/baby/{babyId}")
    public ResponseEntity<List<ProgressDto>> getBabyProgress(@PathVariable UUID babyId) {
        List<ProgressDto> progresses = progressService.getBabyProgress(babyId);
        return ResponseEntity.ok(progresses);
    }

    /**
     * 獲取baby完成的flashcards
     */
    @GetMapping("/baby/{babyId}/flashcards/completed")
    public ResponseEntity<List<ProgressDto>> getBabyCompletedFlashcards(@PathVariable UUID babyId) {
        List<ProgressDto> progresses = progressService.getBabyCompletedFlashcards(babyId);
        return ResponseEntity.ok(progresses);
    }

    /**
     * 獲取baby達成的milestones
     */
    @GetMapping("/baby/{babyId}/milestones/completed")
    public ResponseEntity<List<ProgressDto>> getBabyCompletedMilestones(@PathVariable UUID babyId) {
        List<ProgressDto> progresses = progressService.getBabyCompletedMilestones(babyId);
        return ResponseEntity.ok(progresses);
    }

    /**
     * 獲取baby完成的videos
     */
    @GetMapping("/baby/{babyId}/videos/completed")
    public ResponseEntity<List<ProgressDto>> getBabyCompletedVideos(@PathVariable UUID babyId) {
        List<ProgressDto> progresses = progressService.getBabyCompletedVideos(babyId);
        return ResponseEntity.ok(progresses);
    }

    /**
     * 刪除進度記錄
     */
    @DeleteMapping("/{progressId}")
    public ResponseEntity<Void> deleteProgress(@PathVariable UUID progressId) {
        progressService.deleteProgress(progressId);
        return ResponseEntity.noContent().build();
    }
}
