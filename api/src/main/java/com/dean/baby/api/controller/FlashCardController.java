package com.dean.baby.api.controller;

import com.dean.baby.common.dto.*;
import com.dean.baby.common.service.FlashCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/flashcard")
public class FlashCardController {

    private final FlashCardService flashCardService;

    @Autowired
    public FlashCardController(FlashCardService flashCardService) {
        this.flashCardService = flashCardService;
    }

    // 創建 Flashcard
    @PostMapping
    public ResponseEntity<FlashcardDTO> createFlashcard(@RequestBody FlashcardDTO flashcardDTO) {
        FlashcardDTO createdFlashcard = flashCardService.createFlashcard(flashcardDTO);
        return new ResponseEntity<>(createdFlashcard, HttpStatus.CREATED);
    }

    @PostMapping("check-progress")
    public ResponseEntity<BabyDto> checkProgress(@RequestBody CheckProgressRequestVo vo) {
        return ResponseEntity.ok(flashCardService.checkProgress(vo));
    }

    // 更新 Flashcard
    @PutMapping("{id}")
    public ResponseEntity<FlashcardDTO> updateFlashcard(@PathVariable UUID id, @RequestBody FlashcardDTO flashcardDTO) {
        FlashcardDTO updatedFlashcard = flashCardService.updateFlashcard(id, flashcardDTO);
        return ResponseEntity.ok(updatedFlashcard);
    }

    // 刪除 Flashcard
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable UUID id) {
        flashCardService.deleteFlashcard(id);
        return ResponseEntity.noContent().build();
    }

    // 根據條件查找 FlashCard，支援 ageId 和 categoryId 的組合查詢，無條件時返回全部
    @GetMapping
    public ResponseEntity<List<FlashcardDTO>> searchFlashcards(
            @RequestParam(required = false) UUID ageId,
            @RequestParam(required = false) UUID categoryId) {
        List<FlashcardDTO> flashcards = flashCardService.getFlashcardsByConditions(ageId, categoryId);
        return ResponseEntity.ok(flashcards);
    }
}
