package com.dean.baby.api.controller;

import com.dean.baby.common.dto.FlashcardLanguageDTO;
import com.dean.baby.common.dto.enums.Language;
import com.dean.baby.common.service.FlashCardService;
import com.dean.baby.common.util.LanguageUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/open")
public class OpenController {

    private final FlashCardService flashCardService;

    public OpenController(FlashCardService flashCardService) {
        this.flashCardService = flashCardService;
    }


    @GetMapping("/flash-card")
    public ResponseEntity<List<FlashcardLanguageDTO>> getAllFlashcards() {
        List<FlashcardLanguageDTO> flashcards = flashCardService.getAllFlashcards();
        return ResponseEntity.ok(flashcards);
    }
}
