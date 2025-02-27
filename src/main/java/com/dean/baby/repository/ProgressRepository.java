package com.dean.baby.repository;

import com.dean.baby.entity.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByBabyId(Long babyId);
}
