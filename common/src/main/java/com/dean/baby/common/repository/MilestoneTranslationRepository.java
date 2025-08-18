package com.dean.baby.common.repository;

import com.dean.baby.common.entity.MilestoneTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MilestoneTranslationRepository extends JpaRepository<MilestoneTranslation, UUID> {
    @Query("SELECT mt FROM MilestoneTranslation mt WHERE mt.milestone.ageInMonths = :age AND mt.languageCode = :language")
    List<MilestoneTranslation> findByAgeAndLanguage(int age, String language);
}
