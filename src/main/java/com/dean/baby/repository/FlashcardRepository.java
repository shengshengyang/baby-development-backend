package com.dean.baby.repository;

import com.dean.baby.entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    Optional<Flashcard> findByMilestoneAgeInMonths(Integer ageInMonths);
}
