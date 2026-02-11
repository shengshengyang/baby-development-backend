package com.dean.baby.repository;

import com.dean.baby.entity.Flashcard;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, UUID> {

    @EntityGraph(attributePaths = {"translations", "category", "milestone", "milestone.age"})
    @Override
    List<Flashcard> findAll();

    @EntityGraph(attributePaths = {"translations", "category", "milestone", "milestone.age"})
    Optional<Flashcard> findByMilestoneAgeMonth(Integer month);

    @Query("SELECT f FROM Flashcard f LEFT JOIN FETCH f.translations LEFT JOIN FETCH f.category LEFT JOIN FETCH f.milestone m LEFT JOIN FETCH m.age WHERE f.milestone.id = :milestoneId")
    List<Flashcard> findByMilestoneId(@Param("milestoneId") UUID milestoneId);

    @Query("SELECT f FROM Flashcard f LEFT JOIN FETCH f.translations LEFT JOIN FETCH f.category LEFT JOIN FETCH f.milestone m LEFT JOIN FETCH m.age WHERE m.age.id = :ageId")
    List<Flashcard> findByAgeId(@Param("ageId") UUID ageId);

    @Query("SELECT f FROM Flashcard f LEFT JOIN FETCH f.translations LEFT JOIN FETCH f.category LEFT JOIN FETCH f.milestone m LEFT JOIN FETCH m.age WHERE f.category.id = :categoryId")
    List<Flashcard> findByCategoryId(@Param("categoryId") UUID categoryId);

    @Query("SELECT f FROM Flashcard f LEFT JOIN FETCH f.translations LEFT JOIN FETCH f.category LEFT JOIN FETCH f.milestone m LEFT JOIN FETCH m.age WHERE m.age.id = :ageId AND f.category.id = :categoryId")
    List<Flashcard> findByAgeIdAndCategoryId(@Param("ageId") UUID ageId, @Param("categoryId") UUID categoryId);
}
