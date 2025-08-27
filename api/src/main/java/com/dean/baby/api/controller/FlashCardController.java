package com.dean.baby.api.controller;

import com.dean.baby.common.dto.*;
import com.dean.baby.common.dto.enums.Language;
import com.dean.baby.common.service.FlashCardService;
import com.dean.baby.common.util.LanguageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class FlashCardController {

    private final FlashCardService flashCardService;

    @Autowired
    public FlashCardController(FlashCardService flashCardService) {
        this.flashCardService = flashCardService;
    }

    // 創�� Flashcard
    @PostMapping("/flash-card")
    public ResponseEntity<FlashcardDTO> createFlashcard(@RequestBody FlashcardDTO flashcardDTO) {
        FlashcardDTO createdFlashcard = flashCardService.createFlashcard(flashcardDTO);
        return new ResponseEntity<>(createdFlashcard, HttpStatus.CREATED);
    }


    @PostMapping("/flash-card/check-progress")
    public ResponseEntity<BabyDto> checkProgress(@RequestBody CheckProgressRequestVo vo) {
        return ResponseEntity.ok(flashCardService.checkProgress(vo));
    }

    // 更新 Flashcard
    @PutMapping("/flash-card/{id}")
    public ResponseEntity<FlashcardDTO> updateFlashcard(@PathVariable UUID id, @RequestBody FlashcardDTO flashcardDTO) {
        FlashcardDTO updatedFlashcard = flashCardService.updateFlashcard(id, flashcardDTO);
        return ResponseEntity.ok(updatedFlashcard);
    }

    // 刪除 Flashcard
    @DeleteMapping("/flash-card/{id}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable UUID id) {
        flashCardService.deleteFlashcard(id);
        return ResponseEntity.noContent().build();
    }

    // 根據年齡 ID 查找對應的 FlashCard
    @GetMapping("/flash-card/by-age/{ageId}")
    public ResponseEntity<List<FlashcardDTO>> getFlashcardsByAgeId(@PathVariable UUID ageId) {
        List<FlashcardDTO> flashcards = flashCardService.getFlashcardsByAgeId(ageId);
        return ResponseEntity.ok(flashcards);
    }
}
