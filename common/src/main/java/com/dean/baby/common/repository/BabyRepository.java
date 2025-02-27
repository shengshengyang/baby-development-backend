package com.dean.baby.common.repository;

import com.dean.baby.common.entity.Baby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BabyRepository extends JpaRepository<Baby, Long> {
    List<Baby> findByUserId(Long userId);
}
