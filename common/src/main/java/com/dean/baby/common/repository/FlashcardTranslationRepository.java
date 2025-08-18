package com.dean.baby.common.repository;

import com.dean.baby.common.entity.FlashcardTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlashcardTranslationRepository extends JpaRepository<FlashcardTranslation, UUID> {
    List<FlashcardTranslation> findByFlashcardIdAndLanguageCode(UUID flashcardId, String languageCode);
}
