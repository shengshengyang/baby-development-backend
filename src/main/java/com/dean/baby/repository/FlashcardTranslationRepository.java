package com.dean.baby.repository;

import com.dean.baby.entity.FlashcardTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashcardTranslationRepository extends JpaRepository<FlashcardTranslation, Long> {
    List<FlashcardTranslation> findByFlashcardIdAndLanguageCode(Long flashcardId, String languageCode);
}
