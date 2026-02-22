package com.dean.baby.repository;

import com.dean.baby.entity.Milestone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, UUID> {

    @Query("SELECT DISTINCT m FROM Milestone m " +
           "LEFT JOIN FETCH m.flashcards f " +
           "LEFT JOIN FETCH f.category " +
           "LEFT JOIN FETCH m.age " +
           "LEFT JOIN FETCH m.category")
    @Override
    List<Milestone> findAll();

    @Query("SELECT DISTINCT m FROM Milestone m " +
           "LEFT JOIN FETCH m.flashcards f " +
           "LEFT JOIN FETCH f.category " +
           "LEFT JOIN FETCH m.age " +
           "LEFT JOIN FETCH m.category " +
           "WHERE m.id = :id")
    Optional<Milestone> findByIdWithDetails(@Param("id") UUID id);

    /**
     * 根據年齡 ID 查找里程碑
     */
    @Query("SELECT DISTINCT m FROM Milestone m " +
           "LEFT JOIN FETCH m.flashcards f " +
           "LEFT JOIN FETCH f.category " +
           "LEFT JOIN FETCH m.age " +
           "LEFT JOIN FETCH m.category " +
           "WHERE m.age.id = :ageId")
    List<Milestone> findByAgeId(@Param("ageId") UUID ageId);

    /**
     * 根據年齡 ID 查找里程碑（支援分頁）
     */
    @Query(value = "SELECT m FROM Milestone m WHERE m.age.id = :ageId",
           countQuery = "SELECT count(m) FROM Milestone m WHERE m.age.id = :ageId")
    Page<Milestone> findByAgeId(@Param("ageId") UUID ageId, Pageable pageable);

    /**
     * 根據年齡月份查找里程碑
     */
    @Query("SELECT DISTINCT m FROM Milestone m " +
           "LEFT JOIN FETCH m.flashcards f " +
           "LEFT JOIN FETCH f.category " +
           "LEFT JOIN FETCH m.age " +
           "LEFT JOIN FETCH m.category " +
           "WHERE m.age.month = :month")
    List<Milestone> findByAgeMonth(@Param("month") Integer month);

    /**
     * 根據分類 ID 查找里程碑
     */
    @Query("SELECT DISTINCT m FROM Milestone m " +
           "LEFT JOIN FETCH m.flashcards f " +
           "LEFT JOIN FETCH f.category " +
           "LEFT JOIN FETCH m.age " +
           "LEFT JOIN FETCH m.category " +
           "WHERE m.category.id = :categoryId")
    List<Milestone> findByCategoryId(@Param("categoryId") UUID categoryId);

    /**
     * 根據分類 ID 查找里程碑（支援分頁）
     */
    @Query(value = "SELECT m FROM Milestone m WHERE m.category.id = :categoryId",
           countQuery = "SELECT count(m) FROM Milestone m WHERE m.category.id = :categoryId")
    Page<Milestone> findByCategoryId(@Param("categoryId") UUID categoryId, Pageable pageable);

    /**
     * 根據年齡 ID 和分類 ID 查找里程碑
     */
    @Query("SELECT DISTINCT m FROM Milestone m " +
           "LEFT JOIN FETCH m.flashcards f " +
           "LEFT JOIN FETCH f.category " +
           "LEFT JOIN FETCH m.age " +
           "LEFT JOIN FETCH m.category " +
           "WHERE m.age.id = :ageId AND m.category.id = :categoryId")
    List<Milestone> findByAgeIdAndCategoryId(@Param("ageId") UUID ageId, @Param("categoryId") UUID categoryId);

    /**
     * 根據年齡 ID 和分類 ID 查找里程碑（支援分頁）
     */
    @Query(value = "SELECT m FROM Milestone m WHERE m.age.id = :ageId AND m.category.id = :categoryId",
           countQuery = "SELECT count(m) FROM Milestone m WHERE m.age.id = :ageId AND m.category.id = :categoryId")
    Page<Milestone> findByAgeIdAndCategoryId(@Param("ageId") UUID ageId, @Param("categoryId") UUID categoryId, Pageable pageable);
}
