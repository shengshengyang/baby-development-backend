package com.dean.baby.common.repository;

import com.dean.baby.common.entity.Flashcard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, UUID> {
    Optional<Flashcard> findByMilestoneAgeMonth(Integer month);

    @Query("SELECT f FROM Flashcard f WHERE f.milestone.id = :milestoneId")
    List<Flashcard> findByMilestoneId(@Param("milestoneId") UUID milestoneId);

    @Query("SELECT f FROM Flashcard f WHERE f.milestone.age.id = :ageId")
    List<Flashcard> findByAgeId(@Param("ageId") UUID ageId);
}
