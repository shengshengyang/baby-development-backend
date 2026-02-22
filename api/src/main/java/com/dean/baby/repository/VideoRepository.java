package com.dean.baby.repository;

import com.dean.baby.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {

    @Query("SELECT DISTINCT v FROM Video v " +
           "LEFT JOIN FETCH v.milestone m " +
           "LEFT JOIN FETCH m.age " +
           "LEFT JOIN FETCH m.category " +
           "LEFT JOIN FETCH m.flashcards mf " +
           "LEFT JOIN FETCH mf.translations " +
           "LEFT JOIN FETCH v.article a " +
           "LEFT JOIN FETCH a.translations " +
           "LEFT JOIN FETCH a.category " +
           "LEFT JOIN FETCH v.flashcard f " +
           "LEFT JOIN FETCH f.translations " +
           "LEFT JOIN FETCH f.category " +
           "LEFT JOIN FETCH f.milestone fm " +
           "LEFT JOIN FETCH fm.age " +
           "WHERE v.milestone.id = :milestoneId")
    List<Video> findByMilestoneId(@Param("milestoneId") UUID milestoneId);

    @Query("SELECT DISTINCT v FROM Video v " +
           "LEFT JOIN FETCH v.article a " +
           "LEFT JOIN FETCH a.translations " +
           "LEFT JOIN FETCH a.category " +
           "LEFT JOIN FETCH v.milestone " +
           "LEFT JOIN FETCH v.flashcard " +
           "WHERE v.article.id = :articleId")
    List<Video> findByArticleId(@Param("articleId") UUID articleId);

    @Query("SELECT DISTINCT v FROM Video v " +
           "LEFT JOIN FETCH v.flashcard f " +
           "LEFT JOIN FETCH f.translations " +
           "LEFT JOIN FETCH f.category " +
           "LEFT JOIN FETCH f.milestone fm " +
           "LEFT JOIN FETCH fm.age " +
           "LEFT JOIN FETCH v.milestone " +
           "LEFT JOIN FETCH v.article " +
           "WHERE v.flashcard.id = :flashcardId")
    List<Video> findByFlashcardId(@Param("flashcardId") UUID flashcardId);
}
