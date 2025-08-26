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

    // 創建 Flashcard
    @PostMapping("/flash-card")
    public ResponseEntity<FlashcardDTO> createFlashcard(@RequestBody FlashcardDTO flashcardDTO) {
        List<FlashcardTranslationDTO> translations = flashcardDTO.getTranslations();
        FlashcardDTO createdFlashcard = flashCardService.createFlashcard(
                flashcardDTO.getMilestone().getId(),
                flashcardDTO.getCategory().getId(),
                translations
        );
        return new ResponseEntity<>(createdFlashcard, HttpStatus.CREATED);
    }

    // 讀取單個 Flashcard
    @GetMapping("/open/flash-card/{id}")
    public ResponseEntity<FlashcardDTO> getFlashcardById(@PathVariable UUID id) {
        Optional<FlashcardDTO> flashcardDTO = flashCardService.getFlashcardById(id);
        return flashcardDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/flash-card/check-progress")
    public ResponseEntity<BabyDto> checkProgress(@RequestBody CheckProgressRequestVo vo) {
        return ResponseEntity.ok(flashCardService.checkProgress(vo));
    }

    // 讀取所有 Flashcard (支援多語言，自動從LocaleContextHolder獲取語言設置)
    @GetMapping("/open/flash-cards")
    public ResponseEntity<List<FlashcardLanguageDTO>> getAllFlashCards() {
        Language language = LanguageUtil.getLanguageFromLocale();
        List<FlashcardLanguageDTO> flashcards = flashCardService.getAllFlashcards(language);
        return ResponseEntity.ok(flashcards);
    }

    // 更新 Flashcard
    @PutMapping("/flash-card/{id}")
    public ResponseEntity<FlashcardDTO> updateFlashcard(@PathVariable UUID id, @RequestBody FlashcardDTO flashcardDTO) {
        List<FlashcardTranslationDTO> translations = flashcardDTO.getTranslations();
        FlashcardDTO updatedFlashcard = flashCardService.updateFlashcard(
                id,
                flashcardDTO.getCategory().getId(),
                translations
        );
        return ResponseEntity.ok(updatedFlashcard);
    }

    // 刪除 Flashcard
    @DeleteMapping("/flash-card/{id}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable UUID id) {
        flashCardService.deleteFlashcard(id);
        return ResponseEntity.noContent().build();
    }
}
