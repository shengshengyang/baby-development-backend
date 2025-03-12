package com.dean.baby.common.repository;

import com.dean.baby.common.entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    Optional<Flashcard> findByMilestoneAgeInMonths(Integer ageInMonths);
}
