package com.dean.baby.common.repository;

import com.dean.baby.common.entity.FlashcardTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashcardTranslationRepository extends JpaRepository<FlashcardTranslation, Long> {
    List<FlashcardTranslation> findByFlashcardIdAndLanguageCode(Long flashcardId, String languageCode);
}
