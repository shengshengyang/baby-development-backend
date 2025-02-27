package com.dean.baby.repository;

import com.dean.baby.entity.MilestoneTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MilestoneTranslationRepository extends JpaRepository<MilestoneTranslation, Long> {
    @Query("SELECT mt FROM MilestoneTranslation mt WHERE mt.milestone.ageInMonths = :age AND mt.languageCode = :language")
    List<MilestoneTranslation> findByAgeAndLanguage(int age, String language);
}
