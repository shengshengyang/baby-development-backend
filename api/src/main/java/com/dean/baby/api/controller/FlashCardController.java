package com.dean.baby.api.controller;

import com.dean.baby.common.dto.*;
import com.dean.baby.common.service.FlashCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/flashcard")
@Tag(name = "閃卡管理", description = "寶寶學習閃卡管理 API")
public class FlashCardController {

    private final FlashCardService flashCardService;

    @Autowired
    public FlashCardController(FlashCardService flashCardService) {
        this.flashCardService = flashCardService;
    }

    @Operation(summary = "創建閃卡", description = "創建新的學習閃卡")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "創建成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤")
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FlashcardDTO> createFlashcard(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "閃卡信息") @RequestBody FlashcardDTO flashcardDTO) {
        FlashcardDTO createdFlashcard = flashCardService.createFlashcard(flashcardDTO);
        return new ResponseEntity<>(createdFlashcard, HttpStatus.CREATED);
    }

    @Operation(summary = "檢查進度", description = "檢查寶寶對閃卡的學習進度")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "檢查成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤")
    })
    @PostMapping(value = "check-progress", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BabyDto> checkProgress(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "進度檢查請求") @RequestBody CheckProgressRequestVo vo) {
        return ResponseEntity.ok(flashCardService.checkProgress(vo));
    }

    @Operation(summary = "更新閃卡", description = "更新指定 ID 的閃卡信息")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "更新成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "閃卡不存在")
    })
    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FlashcardDTO> updateFlashcard(
            @Parameter(description = "閃卡 ID") @PathVariable UUID id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "閃卡信息") @RequestBody FlashcardDTO flashcardDTO) {
        FlashcardDTO updatedFlashcard = flashCardService.updateFlashcard(id, flashcardDTO);
        return ResponseEntity.ok(updatedFlashcard);
    }

    @Operation(summary = "刪除閃卡", description = "刪除指定 ID 的閃卡")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "刪除成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "閃卡不存在")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFlashcard(
            @Parameter(description = "閃卡 ID") @PathVariable UUID id) {
        flashCardService.deleteFlashcard(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "查詢閃卡列表", description = "根據年齡段和/或分類查詢閃卡，無條件時返回全部")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查詢成功")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FlashcardDTO>> searchFlashcards(
            @Parameter(description = "年齡段 ID（可選）") @RequestParam(required = false) UUID ageId,
            @Parameter(description = "分類 ID（可選）") @RequestParam(required = false) UUID categoryId) {
        List<FlashcardDTO> flashcards = flashCardService.getFlashcardsByConditions(ageId, categoryId);
        return ResponseEntity.ok(flashcards);
    }
}
