package com.dean.baby.repository;

import com.dean.baby.entity.Baby;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BabyRepository extends JpaRepository<Baby, Long> {
    List<Baby> findByUserId(Long userId);
}
