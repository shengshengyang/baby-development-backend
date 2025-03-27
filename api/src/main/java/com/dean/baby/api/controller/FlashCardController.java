package com.dean.baby.api.controller;

import com.dean.baby.common.dto.CheckProgressRequestVo;
import com.dean.baby.common.dto.FlashcardDTO;
import com.dean.baby.common.dto.FlashcardLanguageDTO;
import com.dean.baby.common.dto.FlashcardTranslationDTO;
import com.dean.baby.common.service.FlashCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
                flashcardDTO.getMilestoneId(),
                flashcardDTO.getCategory(),
                translations
        );
        return new ResponseEntity<>(createdFlashcard, HttpStatus.CREATED);
    }

    // 讀取單個 Flashcard
    @GetMapping("/open/flash-card/{id}")
    public ResponseEntity<FlashcardDTO> getFlashcardById(@PathVariable Long id) {
        Optional<FlashcardDTO> flashcardDTO = flashCardService.getFlashcardById(id);
        return flashcardDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/flash-card/check-progress")
    public ResponseEntity<Void> checkProgress(@RequestBody CheckProgressRequestVo vo) {
        flashCardService.checkProgress(vo);
        return ResponseEntity.noContent().build();
    }

    // 讀取所有 Flashcard

    @GetMapping("/open/flash-card")
    public ResponseEntity<List<FlashcardLanguageDTO>> getAllFlashcards(@RequestHeader("Accept-Language") String language) {
        List<FlashcardLanguageDTO> flashcards = flashCardService.getAllFlashcards(language);
        return ResponseEntity.ok(flashcards);
    }

    // 更新 Flashcard
    @PutMapping("/flash-card/{id}")
    public ResponseEntity<FlashcardDTO> updateFlashcard(@PathVariable Long id, @RequestBody FlashcardDTO flashcardDTO) {
        List<FlashcardTranslationDTO> translations = flashcardDTO.getTranslations();
        FlashcardDTO updatedFlashcard = flashCardService.updateFlashcard(
                id,
                flashcardDTO.getCategory(),
                translations
        );
        return ResponseEntity.ok(updatedFlashcard);
    }

    // 刪除 Flashcard
    @DeleteMapping("/flash-card/{id}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable Long id) {
        flashCardService.deleteFlashcard(id);
        return ResponseEntity.noContent().build();
    }
}
