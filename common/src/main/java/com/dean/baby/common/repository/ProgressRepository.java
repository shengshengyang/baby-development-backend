package com.dean.baby.common.repository;

import com.dean.baby.common.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    @Query("SELECT p FROM Progress p WHERE p.baby.id = :babyId AND p.flashcard.id = :flashcardId")
    Optional<Progress> findByBabyIdAndFlashcardId(@Param("babyId") UUID babyId, @Param("flashcardId") UUID flashcardId);
}
