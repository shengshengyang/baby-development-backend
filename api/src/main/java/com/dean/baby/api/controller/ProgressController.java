package com.dean.baby.api.controller;

import com.dean.baby.common.dto.ProgressDto;
import com.dean.baby.common.dto.UpdateProgressStatusRequest;
import com.dean.baby.common.entity.ProgressStatus;
import com.dean.baby.common.entity.ProgressType;
import com.dean.baby.common.service.ProgressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
     * 統一的進度狀態更新 API
     * 透過 RequestBody 傳入參數，所有邏輯都在 service 處理
     */
    @PostMapping("/update-status")
    public ResponseEntity<ProgressDto> updateProgressStatus(@Valid @RequestBody UpdateProgressStatusRequest request) {
        ProgressDto progress = progressService.updateProgressStatus(request);
        return ResponseEntity.ok(progress);
    }

    /**
     * 更新已存在的進度記錄狀態
     */
    @PutMapping("/{progressId}/status")
    public ResponseEntity<ProgressDto> updateExistingProgressStatus(
            @PathVariable UUID progressId,
            @RequestParam ProgressStatus status,
            @RequestParam(required = false) String date) {

        LocalDate actionDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        ProgressDto progress = progressService.updateProgressStatus(progressId, status, actionDate);
        return ResponseEntity.ok(progress);
    }

    /**
     * 獲取baby的進度，支援多種篩選條件
     */
    @GetMapping("/baby/{babyId}")
    public ResponseEntity<List<ProgressDto>> getBabyProgress(
            @PathVariable UUID babyId,
            @RequestParam(required = false) ProgressStatus status,
            @RequestParam(required = false) ProgressType type) {
        List<ProgressDto> progresses = progressService.getBabyProgress(babyId, status, type);
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
