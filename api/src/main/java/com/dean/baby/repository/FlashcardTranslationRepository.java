package com.dean.baby.repository;

import com.dean.baby.entity.FlashcardTranslation;
import com.dean.baby.dto.enums.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlashcardTranslationRepository extends JpaRepository<FlashcardTranslation, UUID> {
    List<FlashcardTranslation> findByFlashcardIdAndLanguageCode(UUID flashcardId, Language languageCode);
}
