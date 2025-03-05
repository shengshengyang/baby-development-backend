package com.dean.baby.controller;

import com.dean.baby.dto.FlashcardDTO;
import com.dean.baby.dto.FlashcardTranslationDTO;
import com.dean.baby.service.FlashCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/flash-card")
public class FlashCardController {

    private final FlashCardService flashCardService;

    @Autowired
    public FlashCardController(FlashCardService flashCardService) {
        this.flashCardService = flashCardService;
    }

    // 創建 Flashcard
    @PostMapping
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
    @GetMapping("/{id}")
    public ResponseEntity<FlashcardDTO> getFlashcardById(@PathVariable Long id) {
        Optional<FlashcardDTO> flashcardDTO = flashCardService.getFlashcardById(id);
        return flashcardDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 讀取所有 Flashcard
    @GetMapping
    public ResponseEntity<List<FlashcardDTO>> getAllFlashcards(@Nullable @RequestParam  Integer ageInMonths) {
        List<FlashcardDTO> flashcards = flashCardService.getAllFlashcards(ageInMonths);
        return ResponseEntity.ok(flashcards);
    }

    // 更新 Flashcard
    @PutMapping("/{id}")
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlashcard(@PathVariable Long id) {
        flashCardService.deleteFlashcard(id);
        return ResponseEntity.noContent().build();
    }
}
