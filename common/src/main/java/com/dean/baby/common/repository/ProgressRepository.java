package com.dean.baby.common.repository;

import com.dean.baby.common.entity.Progress;
import com.dean.baby.common.entity.ProgressType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, UUID> {
    @Query("SELECT p FROM Progress p WHERE p.baby.id = :babyId AND p.flashcard.id = :flashcardId")
    Optional<Progress> findByBabyIdAndFlashcardId(@Param("babyId") UUID babyId, @Param("flashcardId") UUID flashcardId);

    @Query("SELECT p FROM Progress p WHERE p.baby.id = :babyId")
    List<Progress> findByBabyId(@Param("babyId") UUID babyId);

    @Query("SELECT p FROM Progress p WHERE p.baby.id = :babyId AND p.milestone.id = :milestoneId")
    List<Progress> findByBabyIdAndMilestoneId(@Param("babyId") UUID babyId, @Param("milestoneId") UUID milestoneId);

    @Query("SELECT p FROM Progress p WHERE p.milestone.id = :milestoneId")
    List<Progress> findByMilestoneId(@Param("milestoneId") UUID milestoneId);

    @Query("SELECT p FROM Progress p WHERE p.baby.id = :babyId AND p.flashcard.id = :flashcardId AND p.progressType = :progressType")
    Optional<Progress> findByBabyIdAndFlashcardIdAndProgressType(@Param("babyId") UUID babyId,
                                                                @Param("flashcardId") UUID flashcardId,
                                                                @Param("progressType") ProgressType progressType);

    @Query("SELECT p FROM Progress p WHERE p.baby.id = :babyId AND p.milestone.id = :milestoneId AND p.progressType = :progressType")
    Optional<Progress> findByBabyIdAndMilestoneIdAndProgressType(@Param("babyId") UUID babyId,
                                                                @Param("milestoneId") UUID milestoneId,
                                                                @Param("progressType") ProgressType progressType);

    @Query("SELECT p FROM Progress p WHERE p.baby.id = :babyId AND p.progressType = :progressType AND p.achieved = :achieved")
    List<Progress> findByBabyIdAndProgressTypeAndAchieved(@Param("babyId") UUID babyId,
                                                         @Param("progressType") ProgressType progressType,
                                                         @Param("achieved") boolean achieved);

    @Query("SELECT p FROM Progress p WHERE p.baby.id = :babyId AND p.video.id = :videoId AND p.progressType = :progressType")
    Optional<Progress> findByBabyIdAndVideoIdAndProgressType(@Param("babyId") UUID babyId,
                                                            @Param("videoId") UUID videoId,
                                                            @Param("progressType") ProgressType progressType);
}
