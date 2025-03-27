package com.dean.baby.common.repository;

import com.dean.baby.common.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    Optional<Progress> findByBabyIdAndFlashcardId(Long babyId, Long flashcardId);
}
