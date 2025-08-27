package com.dean.baby.common.repository;

import com.dean.baby.common.entity.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, UUID> {

    /**
     * 根據年齡 ID 查找里程碑
     */
    @Query("SELECT m FROM Milestone m WHERE m.age.id = :ageId")
    List<Milestone> findByAgeId(@Param("ageId") UUID ageId);

    /**
     * 根據年齡月份查找里程碑
     */
    @Query("SELECT m FROM Milestone m WHERE m.age.month = :month")
    List<Milestone> findByAgeMonth(@Param("month") Integer month);
}
