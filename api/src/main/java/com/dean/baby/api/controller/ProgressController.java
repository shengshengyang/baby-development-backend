package com.dean.baby.api.controller;

import com.dean.baby.common.dto.ProgressDto;
import com.dean.baby.common.dto.UpdateProgressStatusRequest;
import com.dean.baby.common.entity.ProgressStatus;
import com.dean.baby.common.entity.ProgressType;
import com.dean.baby.common.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/progress")
@Tag(name = "進度管理", description = "寶寶發展進度追蹤 API")
public class ProgressController {

    private final ProgressService progressService;

    @Autowired
    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @Operation(summary = "更新進度狀態", description = "統一的進度狀態更新 API，透過 RequestBody 傳入參數")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "更新成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤")
    })
    @PostMapping(value = "/update-status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProgressDto> updateProgressStatus(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "進度更新請求") @Valid @RequestBody UpdateProgressStatusRequest request) {
        ProgressDto progress = progressService.updateProgressStatus(request);
        return ResponseEntity.ok(progress);
    }

    @Operation(summary = "更新已有進度狀態", description = "更新已存在的進度記錄狀態")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "更新成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "進度記錄不存在")
    })
    @PutMapping(value = "/{progressId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProgressDto> updateExistingProgressStatus(
            @Parameter(description = "進度記錄 ID") @PathVariable UUID progressId,
            @Parameter(description = "進度狀態") @RequestParam ProgressStatus status,
            @Parameter(description = "操作日期（可選，格式：yyyy-MM-dd）") @RequestParam(required = false) String date) {

        LocalDate actionDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        ProgressDto progress = progressService.updateProgressStatus(progressId, status, actionDate);
        return ResponseEntity.ok(progress);
    }

    @Operation(summary = "獲取寶寶進度列表", description = "獲取指定寶寶的進度記錄，支援多種篩選條件")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "獲取成功")
    })
    @GetMapping(value = "/baby/{babyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProgressDto>> getBabyProgress(
            @Parameter(description = "寶寶 ID") @PathVariable UUID babyId,
            @Parameter(description = "進度狀態（可選）") @RequestParam(required = false) ProgressStatus status,
            @Parameter(description = "進度類型（可選）") @RequestParam(required = false) ProgressType type) {
        List<ProgressDto> progresses = progressService.getBabyProgress(babyId, status, type);
        return ResponseEntity.ok(progresses);
    }

    @Operation(summary = "刪除進度記錄", description = "刪除指定的進度記錄")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "刪除成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "進度記錄不存在")
    })
    @DeleteMapping("/{progressId}")
    public ResponseEntity<Void> deleteProgress(
            @Parameter(description = "進度記錄 ID") @PathVariable UUID progressId) {
        progressService.deleteProgress(progressId);
        return ResponseEntity.noContent().build();
    }
}
