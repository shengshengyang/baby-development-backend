package com.dean.baby.common.repository;

import com.dean.baby.common.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {

    @Query("SELECT v FROM Video v WHERE v.milestone.id = :milestoneId")
    List<Video> findByMilestoneId(@Param("milestoneId") UUID milestoneId);

    @Query("SELECT v FROM Video v WHERE v.article.id = :articleId")
    List<Video> findByArticleId(@Param("articleId") UUID articleId);

    @Query("SELECT v FROM Video v WHERE v.flashcard.id = :flashcardId")
    List<Video> findByFlashcardId(@Param("flashcardId") UUID flashcardId);
}
